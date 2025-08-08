package com.alicebank.finances.entrypoint.api.controller;

import com.alicebank.finances.core.usecase.StatementUseCase;
import com.alicebank.finances.entrypoint.api.controller.contract.StatementContract;
import com.alicebank.finances.entrypoint.api.controller.payload.response.StatementResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/statements")
@RequiredArgsConstructor
public class StatementController implements StatementContract {

    private final StatementUseCase statementUseCase;

    @GetMapping
    @Override
    public StatementResponse searchByCustomerId(@RequestParam Long customerId, @PageableDefault(size = 20) Pageable pageable) {
        var statement = statementUseCase.searchByCustomerId(customerId, pageable);
        return StatementResponse.from(statement);
    }

}
