package miki.assignment.stockservice.lambda.model;

public interface Response<R> {
	public void setStatus(int status);

	public int getStatus();

	public void setMessage(String message);

	public String getMessage();

	public void setReponseObject(R r);

	public R getResponseObject();
}
