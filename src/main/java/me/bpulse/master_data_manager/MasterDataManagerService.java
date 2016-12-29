package me.bpulse.master_data_manager;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;

import me.bpulse.master_data_manager.jsonDomain.Json;

@Component
public class MasterDataManagerService {
	
	// utilidad de logging
	private Logger logger= LoggerFactory.getLogger(MasterDataManagerService.class);
	
	@Value("${bp.endpoint}")
	private String endpoint;
	
	@Value("${bp.masterTypeId}")
	private String masterTypeId;
	
	@Value("${bp.masterDefId}")
	private String masterDefId;
	
	@Value("${bp.login}")
	private String login;
	
	@Value("${bp.password}")
	private String password;
	
	@Autowired
	private PersistenceService persistenceService;

	public void startProcess() {
		logger.info("Starting process...");
		List<String[]> results = persistenceService.getAllResults();
		if (results.size() == 0) return;
		getAndRemoveHeaders(results);
		for (String[] result : results) {
			try 
			{
				insertMaster(result);
			} catch (Exception e) {
				logger.error(e.getMessage() , e);
				System.exit(0);
			}
		}
		logger.info("Num of results processeds: " + results.size());
		logger.info("Process ended");
	}

	private void insertMaster(String[] result) throws Exception {
		//si no hay un masterDef lanza una excepcion 
		if(masterDefId.isEmpty())
		{
			throw new Exception("Debe ingresar un masterDefId");
		}
		if(masterTypeId.isEmpty())
		{
			throw new Exception("Debe ingresar un masterTypeId");
		}
		String json = generateJsonDataRawFromResult(result);
		
		if (sendJsonDataRawToWebservice(endpoint + "api/model/masters/masterdef/"+ masterDefId + "/master/" + result[0], json, "POST") == 422) {
			logger.warn("Se intento crear el maestro con id = "+result[0]+" y masterdef = "+masterDefId+" pero ya existia!!");
			sendJsonDataRawToWebservice(endpoint + "api/model/masters/masterdef/"+ masterDefId + "/master/" + result[0], json, "PUT");
			logger.warn("Se actualizo el maestro con id = "+result[0]+"  masterdef = "+masterDefId);
		}
		createRelationMasterTypeWithMaster(masterTypeId, result[0], masterDefId);
	}

	private void createRelationMasterTypeWithMaster(String masterType, String master, String masterDefId) {
		sendJsonDataRawToWebservice(endpoint + "api/model/masters/masterdef/"+ masterDefId +"/master/"+ master +"/mastertype/"+ masterType, "", "POST");
		
	}

	private int sendJsonDataRawToWebservice(String url, String json, String type) {
		try {
			CredentialsProvider provider = new BasicCredentialsProvider();
			UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(login, password);
			provider.setCredentials(AuthScope.ANY, credentials);

			HttpClient httpClient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
			HttpUriRequest request;
			StringEntity params = new StringEntity(json);
			if (type.equals("POST")) {
				request = new HttpPost(url);
				((HttpPost)request).setEntity(params);
			} else if (type.equals("PUT")) {
				request = new HttpPut(url);
				((HttpPut)request).setEntity(params);
			} else {
				return 999;
			}
			
			request.addHeader("Content-Type", "application/json");
			
			HttpResponse response = httpClient.execute(request);
			return response.getStatusLine().getStatusCode();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 999;
	}

	private String generateJsonDataRawFromResult(String[] result) {
		Json json = new Json();
		json.setName(result[1]);
		json.setDescription(result[0] + " - " + result[1]);
		json.setLatitude(0);
		json.setLongitude(0);
		if (result.length>2) json.setAuxiliary1(result[2]); else json.setAuxiliary1("");
		if (result.length>3) json.setAuxiliary2(result[3]); else json.setAuxiliary2("");
		if (result.length>4) json.setAuxiliary3(result[4]); else json.setAuxiliary3("");
		if (result.length>5) json.setAuxiliary4(result[5]); else json.setAuxiliary4("");
		if (result.length>6) json.setAuxiliary5(result[6]); else json.setAuxiliary5("");
		if (result.length>7) json.setAuxiliary6(result[7]); else json.setAuxiliary6("");
		if (result.length>8) json.setAuxiliary7(result[8]); else json.setAuxiliary7("");
		if (result.length>9) json.setAuxiliary8(result[9]); else json.setAuxiliary8("");
		if (result.length>10) json.setAuxiliary9(result[10]); else json.setAuxiliary9("");
		if (result.length>11) json.setAuxiliary10(result[11]); else json.setAuxiliary10("");
		
		return new Gson().toJson(json);
	}

	private String[] getAndRemoveHeaders(List<String[]> results) {
		if (results.size() > 0)
			return results.remove(0);
		else
			return null;
	}
}
