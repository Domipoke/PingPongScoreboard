package it.domipoke.pingpongscoreboard;

import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions;


class MyFirebase {
   private String IDClient = "473052494821-hl426pj4hiti0kjrokeqf277sg3mim8s.apps.googleusercontent.com";
   private String ClientSecretWeb = "3pVEBEYpUNlOKXHWwZaJkRKk";


   public static BeginSignInRequest signInRequest() {
      String IDClient = "473052494821-hl426pj4hiti0kjrokeqf277sg3mim8s.apps.googleusercontent.com";

      return BeginSignInRequest.builder()
              .setGoogleIdTokenRequestOptions(GoogleIdTokenRequestOptions.builder()
                      .setSupported(true)
                      .setServerClientId(IDClient)
                      .setFilterByAuthorizedAccounts(true)
                      .build())
              .build();
   }
}
