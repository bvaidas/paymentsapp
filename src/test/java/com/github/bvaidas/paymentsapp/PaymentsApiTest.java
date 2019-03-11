package com.github.bvaidas.paymentsapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.github.bvaidas.paymentsapp.api.payload.PaymentRequest;
import com.github.bvaidas.paymentsapp.pojo.Payment;
import com.github.bvaidas.paymentsapp.repository.PaymentsRepository;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.constraints.ConstraintDescriptions;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.RequestDispatcher;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureRestDocs
public class PaymentsApiTest {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Rule
    public final JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PaymentsRepository paymentsRepository;


    private MockMvc mockMvc;

    @Before
    public void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation)).build();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);


    }

    //Static Payment Request generator

    public PaymentRequest createPaymentRequest() {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(new BigDecimal(200));
        paymentRequest.setCurrency("GBP");
        paymentRequest.setPaymentPurpose("Server payment");
        paymentRequest.setSenderName("Wilfred Jeremiah Owens");
        paymentRequest.setSenderAccountNumber("31926819");
        paymentRequest.setSenderAccountNumberCode("BBAN");
        paymentRequest.setReceiverName("Emelia Jane Brown");
        paymentRequest.setReceiverAccountNumber("GB29XABC10161234567801");
        paymentRequest.setReceiverAccountNumberCode("IBAN");
        paymentRequest.setReference("Test reference");

        return paymentRequest;
    }

    @Test
    public void createPayment() throws Exception {

        PaymentRequest paymentRequest = createPaymentRequest();

        String payload = objectMapper.writeValueAsString(paymentRequest);
        log.info("Submitting Payment creation payload: {}", payload);
        ConstraintDescriptions constraintDescriptions = new ConstraintDescriptions(PaymentRequest.class);
        this.mockMvc.perform(post("/v1/payments/").content(payload).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(document("payments-create",
                        requestFields(
                                fieldWithPath("sender_name").description("Sender name").
                                        attributes(key("constrains").value(constraintDescriptions.descriptionsForProperty("sender_name"))),
                                fieldWithPath("sender_account_number").description("Sender account number"),
                                fieldWithPath("sender_account_number_code").description("Account sender name"),
                                fieldWithPath("receiver_name").description("Receiver name"),
                                fieldWithPath("receiver_account_number").description("Receiver Account Number"),
                                fieldWithPath("receiver_account_number_code").description("Receiver Account Number code"),
                                fieldWithPath("payment_purpose").description("Payment Purpose"),
                                fieldWithPath("amount").description("Amount"),
                                fieldWithPath("currency").description("Currency code e.g. GBP"),
                                fieldWithPath("reference").description("Payment reference")
                        )
                        )
                );
    }


    @Test
    public void getPayment() throws Exception {

        //Create payment and then try to retrieve it
        PaymentRequest paymentRequest = createPaymentRequest();

        String paymentRequestPayload = objectMapper.writeValueAsString(paymentRequest);
        MvcResult result = this.mockMvc.perform(post("/v1/payments/").content(paymentRequestPayload).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        Payment payment = objectMapper.readValue(result.getResponse().getContentAsString(), Payment.class);

        MvcResult getPaymentResult = this.mockMvc.perform(get("/v1/payments/{id}", payment.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andDo(document("payments-get",
                        pathParameters(
                                parameterWithName("id").description("Payment unique ID")
                        ))).andReturn();

        Payment getPayment = objectMapper.readValue(getPaymentResult.getResponse().getContentAsString(), Payment.class);

        assertEquals(payment.getId(), getPayment.getId());
    }

    @Test
    public void updatePayment() throws Exception {

        PaymentRequest paymentRequest = createPaymentRequest();

        //Create simple payment
        String paymentRequestPayload = objectMapper.writeValueAsString(paymentRequest);
        log.info("Submitting Payment creation payload: {}", paymentRequestPayload);
        MvcResult result = this.mockMvc.perform(post("/v1/payments/").content(paymentRequestPayload).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();

        //Read Payment Object and use to test update
        Payment payment = objectMapper.readValue(result.getResponse().getContentAsString(), Payment.class);
        payment.setReference("updated reference");

        String payload = objectMapper.writeValueAsString(payment);
        log.info("Submitting Payment update payload: {}", payload);
        this.mockMvc.perform(RestDocumentationRequestBuilders.put("/v1/payments/{id}", payment.getId()).content(payload).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andDo(document("payments-update",
                        pathParameters(
                                parameterWithName("id").description("Payment unique ID")
                        )
                        )
                );
        //Pull payment from database and check result
        Optional<Payment> updatedPayment = paymentsRepository.findById(payment.getId());
        assertEquals(payment.getReference(), updatedPayment.get().getReference());
    }

    @Test
    public void createInvalidEmptyPayment() throws Exception {
        PaymentRequest paymentRequest = new PaymentRequest();
        paymentRequest.setAmount(new BigDecimal(200));
        paymentRequest.setCurrency("GBP");

        String payload = objectMapper.writeValueAsString(paymentRequest);
        log.info("Submitting Invalid Payment creation payload: {}", payload);
        this.mockMvc.perform(post("/v1/payments/").content(payload).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void createPaymentWithNonExistingAccount() throws Exception {

        PaymentRequest paymentRequest = createPaymentRequest();
        paymentRequest.setReceiverAccountNumber("123456");

        String payload = objectMapper.writeValueAsString(paymentRequest);
        log.info("Submitting Invalid Payment creation payload: {}", payload);

        //Reason why this method is so long - https://github.com/spring-projects/spring-restdocs/issues/23
        this.mockMvc.perform(post("/v1/payments/").content(payload).contentType(MediaType.APPLICATION_JSON)).
                andDo(result -> {
                    if (result.getResolvedException() != null) {
                        byte[] response = mockMvc.perform(get("/error").requestAttr(RequestDispatcher.ERROR_STATUS_CODE, result.getResponse()
                                .getStatus())
                                .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, result.getRequest()
                                        .getRequestURI())
                                .requestAttr(RequestDispatcher.ERROR_EXCEPTION, result.getResolvedException())
                                .requestAttr(RequestDispatcher.ERROR_MESSAGE, String.valueOf(result.getResponse()
                                        .getErrorMessage())))
                                .andReturn()
                                .getResponse()
                                .getContentAsByteArray();
                        result.getResponse()
                                .getOutputStream()
                                .write(response);
                    }
                }).
                andExpect(jsonPath("message", is("Payment account not found"))).
                andExpect(status().isPreconditionFailed());
    }

    @Test
    public void createPaymentNegativeAmount() throws Exception {

        PaymentRequest paymentRequest = createPaymentRequest();
        paymentRequest.setAmount(new BigDecimal("-200"));

        String payload = objectMapper.writeValueAsString(paymentRequest);
        log.info("Submitting Invalid Payment creation payload: {}", payload);
        this.mockMvc.perform(post("/v1/payments/").content(payload).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is4xxClientError());
    }

    @Test
    public void deletePaymentStatic() throws Exception {

        //Delete static entry from test data
        UUID uuid = UUID.fromString("987419da-5d19-44bd-bf14-a4756ce80af8");
        //Check that entry exist on DB
        assertTrue(paymentsRepository.findById(uuid).isPresent());

        this.mockMvc.perform(RestDocumentationRequestBuilders.delete("/v1/payments/{id}/", uuid.toString()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(document("payments-delete", pathParameters(
                        parameterWithName("id").description("Payment unique ID")
                )));
        //Check that entry does not exist on DB
        assertFalse(paymentsRepository.findById(uuid).isPresent());
    }

    @Test
    public void deletePayment() throws Exception {

        //Create payment and then try to delete it
        PaymentRequest paymentRequest = createPaymentRequest();

        String paymentRequestPayload = objectMapper.writeValueAsString(paymentRequest);
        MvcResult result = this.mockMvc.perform(post("/v1/payments/").content(paymentRequestPayload).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated()).andReturn();
        Payment payment = objectMapper.readValue(result.getResponse().getContentAsString(), Payment.class);

        this.mockMvc.perform(delete("/v1/payments/{uuid}", payment.getId().toString()).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //Check that entry does not exist on DB
        assertFalse(paymentsRepository.findById(payment.getId()).isPresent());
    }


    @Test
    public void bulkImportTest() throws Exception {

        String p1 = "{\"id\":\"14b5a40e-9121-4cd6-b5ba-74f2333bdad5\",\"version\":0,\"sender\":{\"id\":\"2914fad0-ee9b-4569-b9e3-e3fc20e8360a\",\"account_name\":\"W Owens\",\"account_number\":\"31926819\",\"account_number_code\":\"BBAN\",\"account_type\":0,\"address\":\"1 The Beneficiary Localtown SE2\",\"bank_id\":\"403000\",\"bank_id_code\":\"GBDSC\",\"name\":\"Wilfred Jeremiah Owens\",\"currency\":\"GBP\"},\"receiver\":{\"id\":\"65c27d5f-464a-495c-a470-c85d2a0b616b\",\"account_name\":\"EJ Brown Black\",\"account_number\":\"GB29XABC10161234567801\",\"account_number_code\":\"IBAN\",\"account_type\":null,\"address\":\"10 Debtor Crescent Sourcetown NE1\",\"bank_id\":\"203301\",\"bank_id_code\":\"GBDSC\",\"name\":\"Emelia Jane Brown\",\"currency\":\"GBP\"},\"paymentChargeDetails\":{\"id\":\"fc86e427-16d2-4e23-8cda-f9d966964d82\",\"senderCharges\":[{\"id\":\"11ddd823-8a77-4754-a67b-782932c95035\",\"receiver_charges_amount\":5.0,\"currency\":\"GBP\"},{\"id\":\"e485bd57-154e-4d20-b5dd-88ce121591ad\",\"receiver_charges_amount\":10.0,\"currency\":\"GBP\"}],\"bearer_code\":\"SHAR\",\"sender_charges\":{\"id\":\"87e9893e-df37-436f-ae0d-8840335c1fbf\",\"contract_reference\":\"FX123\",\"exchange_rate\":2.0000,\"original_amount\":200.42,\"original_currency\":\"USD\"},\"receiver_charges_amount\":1.0,\"receiver_charges_currency\":\"GBP\"},\"amount\":200,\"currency\":\"GBP\",\"numeric_reference\":\"1002001\",\"end_to_end_reference\":\"Wil piano Jan\",\"payment_purpose\":\"Server payment\",\"payment_scheme\":\"FPS\",\"payment_type\":\"Credit\",\"processing_date\":\"2019-03-10T23:53:19.798+0000\",\"payment_id\":\"123456789012345678\",\"reference\":\"Test reference\",\"scheme_payment_sub_type\":\"InternetBanking\",\"scheme_payment_type\":\"ImmediatePayment\",\"sponsor_account_number\":\"56781234\",\"sponsor_bank_id\":\"123123\",\"sponsor_bank_id_code\":\"GBDSC\"}\n";
        String p2 = "{\"id\":\"21320039-85d7-4dd2-ab76-f4f3ef1c0bf8\",\"version\":0,\"sender\":{\"id\":\"2914fad0-ee9b-4569-b9e3-e3fc20e8360a\",\"account_name\":\"W Owens\",\"account_number\":\"31926819\",\"account_number_code\":\"BBAN\",\"account_type\":0,\"address\":\"1 The Beneficiary Localtown SE2\",\"bank_id\":\"403000\",\"bank_id_code\":\"GBDSC\",\"name\":\"Wilfred Jeremiah Owens\",\"currency\":\"GBP\"},\"receiver\":{\"id\":\"65c27d5f-464a-495c-a470-c85d2a0b616b\",\"account_name\":\"EJ Brown Black\",\"account_number\":\"GB29XABC10161234567801\",\"account_number_code\":\"IBAN\",\"account_type\":null,\"address\":\"10 Debtor Crescent Sourcetown NE1\",\"bank_id\":\"203301\",\"bank_id_code\":\"GBDSC\",\"name\":\"Emelia Jane Brown\",\"currency\":\"GBP\"},\"paymentChargeDetails\":{\"id\":\"af3c59fa-4313-417e-9599-fa79fcb47c9e\",\"senderCharges\":[{\"id\":\"219e191d-9e4a-4281-a0e1-c4d2c396ff86\",\"receiver_charges_amount\":5.0,\"currency\":\"GBP\"},{\"id\":\"7149646b-6211-4bf8-a388-7b858bc38232\",\"receiver_charges_amount\":10.0,\"currency\":\"GBP\"}],\"bearer_code\":\"SHAR\",\"sender_charges\":{\"id\":\"b5fe7980-16b2-45b2-8a2b-b734f797be61\",\"contract_reference\":\"FX123\",\"exchange_rate\":2.0000,\"original_amount\":200.42,\"original_currency\":\"USD\"},\"receiver_charges_amount\":1.0,\"receiver_charges_currency\":\"GBP\"},\"amount\":200,\"currency\":\"GBP\",\"numeric_reference\":\"1002001\",\"end_to_end_reference\":\"Wil piano Jan\",\"payment_purpose\":\"Server payment\",\"payment_scheme\":\"FPS\",\"payment_type\":\"Credit\",\"processing_date\":\"2019-03-10T23:55:17.537+0000\",\"payment_id\":\"123456789012345678\",\"reference\":\"Test reference\",\"scheme_payment_sub_type\":\"InternetBanking\",\"scheme_payment_type\":\"ImmediatePayment\",\"sponsor_account_number\":\"56781234\",\"sponsor_bank_id\":\"123123\",\"sponsor_bank_id_code\":\"GBDSC\"}\n";

        Payment payment1 = objectMapper.readValue(p1, Payment.class);
        Payment payment2 = objectMapper.readValue(p2, Payment.class);

        //Create new payment which one we are going to update
        PaymentRequest paymentRequest = createPaymentRequest();
        String paymentRequestPayload = objectMapper.writeValueAsString(paymentRequest);
        MvcResult paymentResult = this.mockMvc.perform(post("/v1/payments/").content(paymentRequestPayload).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();
        Payment payment3 = objectMapper.readValue(paymentResult.getResponse().getContentAsString(), Payment.class);

        //update payment3
        payment3.setReference("Updated payment");

        //Create Bulk payload
        List<Payment> payments = new ArrayList<>();
        payments.add(payment1);
        payments.add(payment2);
        payments.add(payment3);

        String payload = objectMapper.writeValueAsString(payments);
        System.out.println(payload);
        MvcResult result = this.mockMvc.perform(patch("/v1/payments/").content(payload).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted()).andExpect(jsonPath("$", hasSize(3)))
                .andDo(document("payments-bulk-import")).andReturn();
    }

    @Test
    public void listPayments() throws Exception {

        MvcResult result = this.mockMvc.perform(get("/v1/payments/").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$.content.[*]", not(emptyArray())))
                .andDo(document("payments-list")).andReturn();
    }


}
