package com.pwoj.as.account.infrastructure.mvc

import com.pwoj.as.AccountServiceApplication
import com.pwoj.as.account.domain.Account
import com.pwoj.as.account.domain.AccountRepository
import com.pwoj.as.account.domain.SubAccount
import com.pwoj.as.account.domain.dto.CurrencyCode
import org.assertj.core.util.Lists
import org.junit.Rule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.restdocs.JUnitRestDocumentation
import org.springframework.restdocs.payload.JsonFieldType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import spock.lang.Specification

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post
import static org.springframework.restdocs.payload.PayloadDocumentation.*
import static org.springframework.restdocs.request.RequestDocumentation.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = AccountServiceApplication.class)
class AccountSpec extends Specification {
    private static final String PESEL = "90121267263"

    @Rule
    private JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation()

    @Autowired
    private WebApplicationContext context

    @Autowired
    private AccountRepository accountRepository

    private MockMvc mockMvc

    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation)).build()
        accountRepository.deleteAll()
    }

    void 'Create account'() {
        expect:
        mockMvc.perform(post("/v1/api/accounts")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"name\": \"name\",\n" +
                        "  \"surname\": \"surname\",\n" +
                        "  \"pesel\": \"92033131422\",\n" +
                        "  \"balance\": \"200.01\"\n" +
                        "}"))
                .andExpect(status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document('create-account',
                        requestFields(
                                [
                                        fieldWithPath('name').type(JsonFieldType.STRING).description('User name.'),
                                        fieldWithPath('surname').type(JsonFieldType.STRING).description('User surname.'),
                                        fieldWithPath('pesel').type(JsonFieldType.STRING).description('User pesel.'),
                                        fieldWithPath('balance').type(JsonFieldType.STRING).description('Account balance in PLN')

                                ])))
    }


    void 'Retrieve account details'() {
        prepareDatabase()

        expect:
        mockMvc.perform(get("/v1/api/accounts/{pesel}", PESEL))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document('get-account-details',
                        pathParameters(parameterWithName("pesel").description("User pesel.")),
                        responseFields(
                                [
                                        fieldWithPath('name').type(JsonFieldType.STRING).description('User name.'),
                                        fieldWithPath('surname').type(JsonFieldType.STRING).description('User surname.'),
                                        fieldWithPath('pesel').type(JsonFieldType.STRING).description('User pesel.'),
                                        fieldWithPath('subAccounts.[].currency').type(JsonFieldType.STRING).description('Sub account currency.'),
                                        fieldWithPath('subAccounts.[].balance').type(JsonFieldType.NUMBER).description('Sub account balance.')
                                ])))
    }

    void 'Exchange money between accounts'() {
        prepareDatabase()

        expect:
        mockMvc.perform(get("/v1/api/accounts/{pesel}/exchange", PESEL)
                .queryParam("sourceCurrency", CurrencyCode.USD.name())
                .queryParam("targetCurrency", CurrencyCode.PLN.name())
                .queryParam("amount", "10.10"))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(document('exchange-money-between-accounts',
                        pathParameters(parameterWithName("pesel").description("User pesel.")),
                        requestParameters(
                                [
                                        parameterWithName("sourceCurrency").description("Source currency."),
                                        parameterWithName("targetCurrency").description("Target currency."),
                                        parameterWithName("amount").description("Amount to exchange."),
                                ]
                        )))

    }

    private void prepareDatabase() {
        def subAccountPln = SubAccount.builder()
                .balance(new BigDecimal("100.01"))
                .currency(CurrencyCode.PLN)
                .build()

        def subAccountUsd = SubAccount.builder()
                .balance(new BigDecimal("200.01"))
                .currency(CurrencyCode.USD)
                .build()


        def account = Account.builder()
                .name("name")
                .surname("surname")
                .pesel(PESEL)
                .subAccounts(Lists.newArrayList(subAccountPln, subAccountUsd))
                .build()
        subAccountUsd.setAccount(account)
        subAccountPln.setAccount(account)

        accountRepository.save(account)
    }
}
