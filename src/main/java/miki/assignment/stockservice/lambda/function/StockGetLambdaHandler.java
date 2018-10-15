package miki.assignment.stockservice.lambda.function;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import miki.assignment.stockservice.dao.IndexDao;
import miki.assignment.stockservice.dao.UserDao;
import miki.assignment.stockservice.dao.impl.IndexDaoImpl;
import miki.assignment.stockservice.dao.impl.UserDaoImpl;
import miki.assignment.stockservice.lambda.model.AbstractRequest;
import miki.assignment.stockservice.lambda.model.AbstractResponse;
import miki.assignment.stockservice.model.Index;
import miki.assignment.stockservice.view.IndexView;

public class StockGetLambdaHandler
		implements RequestHandler<AbstractRequest<Map<String, String>>, AbstractResponse<List<IndexView>>> {

	@Override
	public AbstractResponse<List<IndexView>> handleRequest(AbstractRequest<Map<String, String>> input,
			Context context) {
		AbstractResponse<List<IndexView>> response = new AbstractResponse<List<IndexView>>();

		try {
			boolean validate = validateUser(input);
			if (validate) {
				IndexDao indexDao = createIndexDao();

				Map<String, String> setMap = input.getRequestObject();
				String setName = setMap.get("setName");
				String queryDate = setMap.get("queryDate");
				Timestamp currentTimestamp = null;

				if (queryDate != null) {
					currentTimestamp = convertTimestamp(queryDate);
				} else {
					currentTimestamp = getCurrentTimestamp();
				}

				List<Index> indexList = indexDao.listIndexInDate(setName, currentTimestamp);
				context.getLogger().log("Index size: " + indexList.size());
				List<IndexView> indexViewList = convertToView(indexList);
				context.getLogger().log("Index View size: " + indexViewList.size());
				response.setStatus(1);
				response.setMessage("Success!");
				response.setReponseObject(indexViewList);

			} else {
				response.setStatus(0);
				response.setMessage("Invalidate username/password");
			}

		} catch (

		SQLException sql) {
			response.setStatus(0);
			response.setMessage("SQLException :" + sql.getMessage());

		} catch (ParseException e) {
			response.setStatus(0);
			response.setMessage("Received Date not in format yyyy-MM-dd");
		}
		return response;
	}

	private List<IndexView> convertToView(List<Index> indexList) {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		List<IndexView> indexViewList = new ArrayList<IndexView>();
		for (Index index : indexList) {
			IndexView indexView = new IndexView();
			indexView.setCode(index.getCode());
			indexView.setLast(index.getLast());
			indexView.setReceiveDate(simpleDateFormat.format(index.getReceiveDate()));

			indexViewList.add(indexView);
		}

		return indexViewList;
	}

	private Timestamp getCurrentTimestamp() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		Timestamp current = new Timestamp(calendar.getTimeInMillis());

		return current;
	}

	private Timestamp convertTimestamp(String receivedDate) throws ParseException {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

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

	private boolean validateUser(AbstractRequest<Map<String, String>> input) throws SQLException {
		String username = input.getUsername();
		String password = input.getPassword();

		UserDao userDao = createUserDao();
		boolean validate = userDao.validateUser(username, password);

		return validate;
	}

}
