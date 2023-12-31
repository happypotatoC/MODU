package com.modu.app.sms.service;

import java.io.UnsupportedEncodingException;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;

@PropertySource("classpath:application.properties")
@Slf4j
@RequiredArgsConstructor
@Service
public class SmsService {
	// 휴대폰 인증 번호

	@Value("${naver-cloud-sms.accessKey}")
	private String accessKey;

	@Value("${naver-cloud-sms.secretKey}")
	private String secretKey;

	@Value("${naver-cloud-sms.serviceId}")
	private String serviceId;

	@Value("${naver-cloud-sms.senderPhone}")
	private String phone;

	private String smsConfirmNum;

	public String getSignature(String time)
			throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
		String space = " ";
		String newLine = "\n";
		String method = "POST";
		String url = "/sms/v2/services/" + this.serviceId + "/messages";
		String accessKey = this.accessKey;
		String secretKey = this.secretKey;

		String message = new StringBuilder().append(method).append(space).append(url).append(newLine).append(time)
				.append(newLine).append(accessKey).toString();

		SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(signingKey);

		byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
		String encodeBase64String = Base64.encodeBase64String(rawHmac);

		return encodeBase64String;
	}

	public SmsResponseDTO sendSms(MessageDTO messageDTO) throws JsonProcessingException, RestClientException,
			URISyntaxException, InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException {
		String time = Long.toString(System.currentTimeMillis());
		String smsConfirmNum = createSmsKey();
		this.smsConfirmNum = smsConfirmNum;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("x-ncp-apigw-timestamp", time);
		headers.set("x-ncp-iam-access-key", accessKey);
		headers.set("x-ncp-apigw-signature-v2", getSignature(time)); // signature 서명

		List<MessageDTO> messages = new ArrayList<>();
		messages.add(messageDTO);

		SmsRequestDTO request = SmsRequestDTO.builder().type("SMS").contentType("COMM").countryCode("82").from(phone)
				.content("인증번호 [" + smsConfirmNum + "]").messages(messages).build();

		// 쌓은 바디를 json형태로 반환
		ObjectMapper objectMapper = new ObjectMapper();
		String body = objectMapper.writeValueAsString(request);
		// jsonBody와 헤더 조립
		HttpEntity<String> httpBody = new HttpEntity<>(body, headers);

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
		// restTemplate로 post 요청 보내고 오류가 없으면 202코드 반환
		SmsResponseDTO smsResponseDTO = restTemplate.postForObject(
				new URI("https://sens.apigw.ntruss.com/sms/v2/services/" + serviceId + "/messages"), httpBody,
				SmsResponseDTO.class);
		SmsResponseDTO responseDTO = new SmsResponseDTO(smsConfirmNum);
		return smsResponseDTO;
	}

	// 인증코드 만들기
	private String createSmsKey() {
		StringBuffer key = new StringBuffer();
		Random rnd = new Random();

		for (int i = 0; i < 5; i++) { // 인증코드 5자리
			key.append((rnd.nextInt(10)));
		}
		return key.toString();
	}

	public boolean isSmsCodeValid(String inputSmsCode) {
		return smsConfirmNum != null && smsConfirmNum.equals(inputSmsCode);
	}

}
