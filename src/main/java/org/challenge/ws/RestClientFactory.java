package org.challenge.ws;

public class RestClientFactory {

    private static RestClientFactory INSTANCE;

    public static RestClientFactory getInstance(){
        if (INSTANCE == null) {
            INSTANCE = new RestClientFactory();
        }
        return INSTANCE;
    }

    private RestClientFactory() {
        super();
    }

    public RestClient create(String apiKey){
        return new RestClientFaireImpl(apiKey);
    }
}
