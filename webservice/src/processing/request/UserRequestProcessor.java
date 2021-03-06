package processing.request;

import java.util.List;

import loggin.JavaLogger;

import com.google.gson.Gson;
import com.sun.jersey.api.client.ClientResponse;

import processing.response.deserialize.ContributorsDeserializer;
import processing.response.deserialize.UserDeserializerHelper;
import processing.response.model.User;
import api.communication.RequestSender;
import aspects.StatusCodeHandler;

public class UserRequestProcessor {

	public static String getUser(String userName, String token){
		//System.out.println("Request Processor| GET USER| " + userName);
		String request=URL.API+URL.USERS+userName;
		
		request=AuthorizationProcessor.addAuthorization(request, token);
		
		JavaLogger.log("UserRequestProcessor| getUser| User| " + userName + " URL| " + request);
		ClientResponse serverResponse= RequestSender.sendRequest(request);
		
		if(serverResponse != null){
		JavaLogger.log("UserRequestProcessor| getUser| ServerResponse| " + serverResponse);
		Gson gson=new Gson();
		
		UserDeserializerHelper deserializedUser = gson.fromJson(serverResponse.getEntity(String.class), UserDeserializerHelper.class);
		JavaLogger.log("UserRequestProcessor| getUser| Plain_DesralizedUser| " + deserializedUser.toString());
		User user = deserializedUser.toGenuineUser();
		
			return gson.toJson(user);
		}
		else
		{
			System.out.println("UserRequestProcessor|getUser|Error|");
			return StatusCodeHandler.jsonErrorGenerator();
		}
//		return user.toString();
	}

}
