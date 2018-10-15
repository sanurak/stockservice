package miki.assignment.stockservice.lambda.function;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import miki.assignment.stockservice.dao.IndexDao;
import miki.assignment.stockservice.dao.UserDao;
import miki.assignment.stockservice.dao.impl.IndexDaoImpl;
import miki.assignment.stockservice.dao.impl.UserDaoImpl;
import miki.assignment.stockservice.lambda.model.AbstractRequest;
import miki.assignment.stockservice.lambda.model.AbstractResponse;
import miki.assignment.stockservice.model.Index;

public class StockUpdateLambdaHandler
		implements RequestHandler<AbstractRequest<List<Map<String, String>>>, AbstractResponse<String>> {

	@Override
	public AbstractResponse<String> handleRequest(AbstractRequest<List<Map<String, String>>> input, Context context) {
		AbstractResponse<String> response = new AbstractResponse<String>();

		try {
			boolean validate = validateUser(input);
			if (validate) {
				IndexDao indexDao = createIndexDao();

				Set<String> targetStockIndex = indexDao.getTargetStockIndex();

				List<Index> indexList = new ArrayList<Index>();

				for (Map<String, String> setMap : input.getRequestObject()) {
					String setName = setMap.get("setName");
					String setValue = setMap.get("setValue");
					String receiveDate = setMap.get("receivedDate");
					Timestamp currentTimestamp = null;

					if (targetStockIndex.contains(setName)) {
						if (receiveDate != null) {
							currentTimestamp = convertTimestamp(receiveDate);
						} else {
							currentTimestamp = getCurrentTimestamp();
						}

						Index index = new Index();
						index.setCode(setName);
						index.setLast(new BigDecimal(setValue));
						index.setReceiveDate(currentTimestamp);

						indexList.add(index);
					}
				}
				indexDao.updateIndex(indexList);

				response.setStatus(1);
				response.setMessage("Success!");

			} else {
				response.setStatus(0);
				response.setMessage("Invalidate username/password");
			}

		} catch (SQLException sql) {
			response.setStatus(0);
			response.setMessage("SQLException :" + sql.getMessage());

		} catch (ParseException e) {
			response.setStatus(0);
			response.setMessage("Received Date not in format yyyy-MM-dd HH:mm");
		}
		return response;
	}

	private Timestamp getCurrentTimestamp() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Timestamp current = new Timestamp(calendar.getTimeInMillis());

		return current;
	}

	private Timestamp convertTimestamp(String receivedDate) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		Date date = simpleDateFormat.parse(receivedDate);

		Timestamp timestamp = new Timestamp(date.getTime());

		return timestamp;

	}

	private UserDao createUserDao() {
		String endPoint = System.getenv("endPoint");
		String dbName = System.getenv("dbName");
		String username = System.getenv("username");
		String password = System.getenv("password");

		UserDao userDao = new UserDaoImpl(endPoint, dbName, username, password);

		return userDao;
	}

	private IndexDao createIndexDao() {
		String endPoint = System.getenv("endPoint");
		String dbName = System.getenv("dbName");
		String username = System.getenv("username");
		String password = System.getenv("password");

		IndexDaoImpl indexDao = new IndexDaoImpl(endPoint, dbName, username, password);

		return indexDao;
	}

	private boolean validateUser(AbstractRequest<List<Map<String, String>>> input) throws SQLException {
		String username = input.getUsername();
		String password = input.getPassword();

		UserDao userDao = createUserDao();
		boolean validate = userDao.validateUser(username, password);

		return validate;
	}
}
