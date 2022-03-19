package com.ajclaros.payoutsapp.wakanda;

import com.ajclaros.payoutsapp.client.payout.PayoutClient;
import com.ajclaros.payoutsapp.client.payout.PayoutDto;
import com.ajclaros.payoutsapp.client.payout.PayoutDtoMapper;
import com.ajclaros.payoutsapp.file.PathFinder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

@Slf4j
@Service
public class WakandaService {

    private final WakandaPayoutMapper wakandaPayoutMapper;
    private final PayoutDtoMapper payoutDtoMapper;
    private final PayoutClient payoutClient;
    private final String filePath;
    private final String fileStartsWith;

    public WakandaService(WakandaPayoutMapper wakandaPayoutMapper, PayoutDtoMapper payoutDtoMapper, PayoutClient payoutClient,
                          @Value("${wakanda.file.path}") String filePath, @Value("${wakanda.file.starts-with}") String fileStartsWith) {
        this.wakandaPayoutMapper = wakandaPayoutMapper;
        this.payoutDtoMapper = payoutDtoMapper;
        this.payoutClient = payoutClient;
        this.filePath = filePath;
        this.fileStartsWith = fileStartsWith;
    }

    @Scheduled(cron = "${wakanda.cron.expression}", zone = "${wakanda.cron.zone}")
    public void payout() {
        log.info("Executing payouts for Wakanda...");

        try (Stream<String> stream = Files.lines(getPath())) {
            stream.skip(1)
                    .map(toWakandaPayout())
                    .filter(Objects::nonNull)
                    .map(toPayoutDto())
                    .forEach(sendToExternalPayoutApiForProcessing());
        } catch (IOException e) {
            log.error("Could not read the input file!", e);
            log.error("Execution failed!");
            return;
        }
        log.info("Execution finished!");
    }

    @SneakyThrows
    private Path getPath() {
        // String yesterday = now().minusDays(1).format(DateTimeFormatter.BASIC_ISO_DATE);
        String yesterday = "20220317";
        return PathFinder.getPath(filePath, fileStartsWith + yesterday);
    }

    private Function<String, WakandaPayout> toWakandaPayout() {
        return str -> {
            try {
                WakandaPayout wakandaPayout = wakandaPayoutMapper.map(str);
                log.info("Received payout {} from wakanda file mapping to dto.", wakandaPayout);
                return wakandaPayout;
            } catch (Exception e) {
                log.warn("Could not map to wakanda payout {}", str, e);
                return null;
            }
        };
    }

    private Function<WakandaPayout, PayoutDto> toPayoutDto() {
        return payout -> {
            log.info("Received payout {} from wakanda file mapping to dto.", payout);
            return payoutDtoMapper.map(payout);
        };
    }

    private Consumer<PayoutDto> sendToExternalPayoutApiForProcessing() {
        return dto -> {
            log.info("Sending dto {} to external api for processing.", dto);
            ResponseEntity<String> response = payoutClient.postPayout(dto);
            if (!response.getStatusCode().isError()) {
                log.info("External processing succeeded: {}", response.getStatusCode());
            } else {
                log.warn("External processing failed: {}", response.getStatusCode());
            }
        };
    }

}
