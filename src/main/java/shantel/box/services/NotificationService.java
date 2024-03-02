package shantel.box.services;

public interface NotificationService {

	void sendPushNotificationToSender(String senderPushToken, String message);
	
}
