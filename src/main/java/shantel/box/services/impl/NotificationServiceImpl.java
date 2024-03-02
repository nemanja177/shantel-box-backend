package shantel.box.services.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import shantel.box.services.NotificationService;

@Service
public class NotificationServiceImpl implements NotificationService {

	@Override
	public void sendPushNotificationToSender(String senderPushToken, String message) {
		 String notificationJson = String.format("{\"to\": \"%s\", \"sound\": \"default\", \"title\": \"Shantel Kutija\", \"body\": \"%s\"}", senderPushToken, message);

	     RestTemplate restTemplate = new RestTemplate();
	     HttpHeaders headers = new HttpHeaders();
	     headers.setContentType(MediaType.APPLICATION_JSON);

	     HttpEntity<String> request = new HttpEntity<>(notificationJson, headers);

	     String response = restTemplate.postForObject("https://exp.host/--/api/v2/push/send", request, String.class);
	     System.out.println("Expo Push Notification Response: " + response);
		
	}

}
