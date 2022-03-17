package com.ajclaros.payoutsapp.wakanda;

import com.ajclaros.payoutsapp.client.payout.PayoutClient;
import com.ajclaros.payoutsapp.client.payout.PayoutDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Service
public class WakandaService {

    private static final String FILE_PATH = "src/main/resources/wakanda/";
    private static final String FILE_NAME = "wakanda-payouts.txt";

    private final PayoutClient payoutClient;
    private final WakandaPayoutMapper wakandaPayoutMapper;
    private final PayoutDtoMapper payoutDtoMapper;

    public WakandaService(PayoutClient payoutClient, WakandaPayoutMapper wakandaPayoutMapper, PayoutDtoMapper payoutDtoMapper) {
        this.payoutClient = payoutClient;
        this.wakandaPayoutMapper = wakandaPayoutMapper;
        this.payoutDtoMapper = payoutDtoMapper;
    }

    @Scheduled(cron = "${wakanda.cron.expression}", zone = "${wakanda.cron.zone}")
    public void payout() {
        log.info("Executing payouts for Wakanda...");
        try (Stream<String> stream = Files.lines(Paths.get(FILE_PATH + FILE_NAME))) {
            stream.skip(1)
                    .map(wakandaPayoutMapper::map)
                    .filter(Objects::nonNull)
                    .map(payout -> {
                        log.info("Received payout {} from wakanda file mapping to dto.", payout);
                        return payoutDtoMapper.map(payout);
                    })
                    .map(dto -> {
                        log.info("Sending dto {} to external api for processing.", dto);
                        return payoutClient.postPayout(dto);
                    })
                    .forEach(response -> {
                        if (!response.getStatusCode().isError()) {
                            log.info("External processing succeeded: {}", response.getStatusCode());
                        } else {
                            log.warn("External processing failed: {}", response.getStatusCode());
                        }
                    });
        } catch (IOException e) {
            log.error("Could not read the input file!", e);
            log.info("Execution failed!", e);
            return;
        }
        log.info("Execution finished!");
    }

}
