package com.ajclaros.payoutsapp.wakanda;

import com.ajclaros.payoutsapp.client.payout.PayoutClient;
import com.ajclaros.payoutsapp.client.payout.PayoutDto;
import com.ajclaros.payoutsapp.client.payout.PayoutDtoMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.Random;

import static com.ajclaros.payoutsapp.wakanda.WakandaPayoutStatusEnum.PENDING;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class WakandaServiceTest {
    private static final String FILE_PATH = "src/test/resources/wakanda-service/";
    private static final String FILE_STARTS_WITH = "WakandaServiceTest_";

    private static final WakandaPayout WAKANDA_PAYOUT_1 = createWakandaPayout(1);
    private static final WakandaPayout WAKANDA_PAYOUT_2 = createWakandaPayout(2);

    private static final PayoutDto PAYOUT_DTO_1 = createPayoutDto(1);
    private static final PayoutDto PAYOUT_DTO_2 = createPayoutDto(2);

    private static final ResponseEntity<String> RESPONSE_ENTITY_OK = new ResponseEntity<>(HttpStatus.OK);
    private static final ResponseEntity<String> RESPONSE_ENTITY_ERROR = new ResponseEntity<>(HttpStatus.NOT_FOUND);

    @Mock
    private WakandaPayoutMapper wakandaPayoutMapper;
    @Mock
    private PayoutDtoMapper payoutDtoMapper;
    @Mock
    private PayoutClient payoutClient;

    private WakandaService wakandaService;

    @BeforeEach
    public void setUp() {
        wakandaService = new WakandaService(wakandaPayoutMapper, payoutDtoMapper, payoutClient, FILE_PATH, FILE_STARTS_WITH);
    }

    @Test
    @SneakyThrows
    void givenAnInvalidFileWhenCallingExecutePayoutsShouldFinishExecution() {
        wakandaService = new WakandaService(wakandaPayoutMapper, payoutDtoMapper, payoutClient, "wrongPath", "wrongFile");
        wakandaService.executePayouts();

        verify(wakandaPayoutMapper, never()).map(any());
        verify(payoutDtoMapper, never()).map(any());
        verify(payoutClient, never()).postPayout(any());
    }

    @Test
    @SneakyThrows
    void givenAFileWithAllBadFormattedLinesWhenCallingExecutePayoutsShouldSkipThemAll() {
        when(wakandaPayoutMapper.map("line1")).thenThrow(IllegalArgumentException.class);
        when(wakandaPayoutMapper.map("line2")).thenThrow(IllegalArgumentException.class);

        wakandaService.executePayouts();

        verify(wakandaPayoutMapper, times(2)).map(any());
        verify(payoutDtoMapper, never()).map(any());
        verify(payoutClient, never()).postPayout(any());
    }

    @Test
    @SneakyThrows
    void givenAFileWithABadFormattedLineWhenCallingExecutePayoutsShouldSkipThatLine() {
        when(wakandaPayoutMapper.map("line1")).thenReturn(WAKANDA_PAYOUT_1);
        when(payoutDtoMapper.map(WAKANDA_PAYOUT_1)).thenReturn(PAYOUT_DTO_1);
        when(payoutClient.postPayout(PAYOUT_DTO_1)).thenReturn(RESPONSE_ENTITY_OK);

        when(wakandaPayoutMapper.map("line2")).thenThrow(IllegalArgumentException.class);

        wakandaService.executePayouts();

        verify(wakandaPayoutMapper, times(2)).map(any());
        verify(payoutDtoMapper, times(1)).map(WAKANDA_PAYOUT_1);
        verify(payoutClient, times(1)).postPayout(PAYOUT_DTO_1);

        verify(payoutDtoMapper, never()).map(WAKANDA_PAYOUT_2);
        verify(payoutClient, never()).postPayout(PAYOUT_DTO_2);
    }

    @Test
    @SneakyThrows
    void givenProperFileWhenCallingExecutePayoutsShouldSendAllPayoutsToExternalApi() {
        when(wakandaPayoutMapper.map("line1")).thenReturn(WAKANDA_PAYOUT_1);
        when(wakandaPayoutMapper.map("line2")).thenReturn(WAKANDA_PAYOUT_2);

        when(payoutDtoMapper.map(WAKANDA_PAYOUT_1)).thenReturn(PAYOUT_DTO_1);
        when(payoutDtoMapper.map(WAKANDA_PAYOUT_2)).thenReturn(PAYOUT_DTO_2);

        when(payoutClient.postPayout(PAYOUT_DTO_1)).thenReturn(RESPONSE_ENTITY_OK);
        when(payoutClient.postPayout(PAYOUT_DTO_2)).thenReturn(RESPONSE_ENTITY_ERROR);

        wakandaService.executePayouts();

        verify(wakandaPayoutMapper, times(2)).map(any());
        verify(payoutDtoMapper, times(2)).map(any());
        verify(payoutClient, times(2)).postPayout(any());
    }

    private static WakandaPayout createWakandaPayout(int id) {
        Random random = new Random();
        return WakandaPayout.builder()
                .companyName("companyName" + id)
                .companyTaxNumber("companyTaxNumber" + id)
                .status(PENDING)
                .paymentDate(LocalDate.now())
                .amount(random.nextDouble())
                .build();
    }

    private static PayoutDto createPayoutDto(int id) {
        Random random = new Random();
        return PayoutDto.builder()
                .companyIdentityNumber("companyName" + id)
                .paymentDate("paymentDate")
                .paymentAmount(random.nextDouble())
                .build();
    }


}
