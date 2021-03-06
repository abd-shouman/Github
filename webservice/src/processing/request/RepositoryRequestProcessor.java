package processing.request;

import java.util.List;

import loggin.JavaLogger;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.jersey.api.client.ClientResponse;

import processing.response.deserialize.ContributorsDeserializer;
import processing.response.deserialize.ContributorsDeserializerHelper;
import processing.response.deserialize.RepositoryDeseralizerHelper;
import processing.response.model.Commit;
import processing.response.model.Repository;
import processing.response.model.User;
import api.communication.RequestSender;
import aspects.StatusCodeHandler;

public class RepositoryRequestProcessor {
	
	public static String getRepositoryInfo(String userName, String repoName, String token){
		String request=URL.API+URL.REPOS+userName+"/"+repoName+URL.PER_PAGE;
		JavaLogger.log("RepositoryRequestProcessor| getRepositoryInfo| User| " + userName + " Repo| " + repoName + " Url| " + request);
		
		AuthorizationProcessor.addAuthorization(request, token);
		
		ClientResponse serverResponse=RequestSender.sendRequest(request);
		
		if(serverResponse != null){
		
		Gson gson=new Gson();
		
		RepositoryDeseralizerHelper deserializer=gson.fromJson(serverResponse.getEntity(String.class), RepositoryDeseralizerHelper.class);
		Repository repo=deserializer.toGenuineRepository();
//		return repo.toString();
		
		String json=gson.toJson(repo);
		return json;
		}
		else{
			System.out.println("RepositoryRequestProcessor|getRepositoryInfo|Error|");
			return StatusCodeHandler.jsonErrorGenerator();
		}
	}

	public static String getRepositoryCommitsDistribution(String userName,String repoName, String token) {
		String request=URL.API+URL.REPOS+userName+"/"+repoName+URL.COMMITS_DISTRIBUTION;
		JavaLogger.log("RepositoryRequestProcessor| getRepositoryCommitsDistribution| User| " + userName + " Repo| " + repoName + " Url| " + request);
		
		AuthorizationProcessor.addAuthorization(request, token);
		ClientResponse serverResponse=RequestSender.sendRequest(request);
		
		if(serverResponse != null){
		
		Gson gson=new Gson();
		List<int[]> deserializedList=gson.fromJson(serverResponse.getEntity(String.class), new TypeToken<List<int[]>>(){}.getType());
		int[][] days=new int[7][24];
		for (int[] is : deserializedList) {
				days[is[0]][is[1]]=is[2];
		}
		Commit comm=new Commit();
		comm.setCommitDistribution(days);
		
		return gson.toJson(comm.getCommitDistribution());
		}
		else{
			System.out.println("RepositoryRequestProcessor|getRepositoryCommitsDistribution|Error|");
			return StatusCodeHandler.jsonErrorGenerator();
		}
		
//		return comm.printCommitDistribution();
	}
	
	public static String getCommitInfo(String userName, String repoName, String token){
		String request=URL.API+URL.REPOS+userName+"/"+repoName;
		JavaLogger.log("RepositoryRequestProcessor| getCommitInfo| User| " + userName + " Repo| " + repoName + " Url| " + request);
		
		request=AuthorizationProcessor.addAuthorization(request, token);
		ClientResponse serverResponse=RequestSender.sendRequest(request);
		
		if(serverResponse != null){
			
		Gson gson=new Gson();
		Commit comm=gson.fromJson(serverResponse.getEntity(String.class), Commit.class);
		
		return gson.toJson(comm);
		}
		else{
			System.out.println("RepositoryRequestProcessor|getCommitInfo|Error|");
			return StatusCodeHandler.jsonErrorGenerator();
		}
		
//		return comm.toString();
	}

}
