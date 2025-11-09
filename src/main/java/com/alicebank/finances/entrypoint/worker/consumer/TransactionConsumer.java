package com.alicebank.finances.entrypoint.worker.consumer;

import com.alicebank.finances.core.usecase.TransactionUseCase;
import com.alicebank.finances.entrypoint.worker.payload.TransactionEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import static com.alicebank.finances.entrypoint.worker.config.WorkerConfig.TRANSACTION_QUEUE;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionConsumer {

    private final TransactionUseCase transactionUseCase;
    private final ObjectMapper mapper;

    @RabbitListener(queues = TRANSACTION_QUEUE)
    public void consumer(byte[] bytes) {
        try {
            var event = mapper.readValue(bytes, TransactionEvent.class);
            transactionUseCase.createTransfer(event.toTransaction());
        } catch (Exception e) {
            log.error("Erro ao processar transação", e);
        }
    }

}
