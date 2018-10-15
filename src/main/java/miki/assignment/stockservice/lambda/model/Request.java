package miki.assignment.stockservice.lambda.model;

public interface Request<R> {
	public void setUsername(String username);

	public String getUsername();

	public void setPassword(String password);

	public String getPassword();

	public void setRequestObject(R r);

	public R getRequestObject();

}
