package fr.slaynash.creatibox.client.connection;

public interface ConnectionStateHandler {
	public void handleConnectException(Exception e);
	public void connectionFinished();
	public void connectionStateChanged(int state);
}
