package rpc;

import java.io.IOException;
import java.util.ArrayList;
//import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
//import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;
//import external.TicketMasterAPI;

@WebServlet("/search")
public class SearchItem extends HttpServlet { 
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String userId = request.getParameter("user_id");
	
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));
		// Term can be empty or null.
		String term = request.getParameter("term");

		DBConnection connection = DBConnectionFactory.getConnection();
		List<Item> items = connection.searchItems(lat, lon, term);
 		connection.close();
 		
 		Set<String> favorite = connection.getFavoriteItemIds(userId);
		connection.close();


		List<JSONObject> list = new ArrayList<>();
		try {
			for (Item item : items) {
				// Add a thin version of item object
				JSONObject obj = item.toJSONObject();
				
				// Check if this is a favorite one.
				// This field is required by frontend to correctly display favorite items.
				obj.put("favorite", favorite.contains(item.getItemId()));

				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray array = new JSONArray(list);
		RpcHelper.writeJsonArray(response, array);
	}
}


	/*
	 @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
*/
 