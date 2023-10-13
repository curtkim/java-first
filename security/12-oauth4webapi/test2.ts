/*
(async () => {
    const response = await fetch('https://quotes.toscrape.com/random');
    const body = await response.text();
    console.log(body);
})();
*/
import * as oauth from 'oauth4webapi'


async function doit() {

  const issuer = new URL('https://accounts.google.com/o/oauth2/v2/auth')
  const as = await oauth
    .discoveryRequest(issuer)
    .then((response) => {
      oauth.processDiscoveryResponse(issuer, response)
    })

  const client: oauth.Client = {
    client_id: '248784500218-eii659iq8256iec7531oufn5aejnropn.apps.googleusercontent.com',
    token_endpoint_auth_method: 'client_secret_basic',
  }

  const redirect_uri = 'http://localhost:8080/authorized'
  console.log(as.code_challenge_methods_supported)

  if (as.code_challenge_methods_supported?.includes('S256') !== true) {
    // This example assumes S256 PKCE support is signalled
    // If it isn't supported, random `nonce` must be used for CSRF protection.
    throw new Error()
  }

  const code_verifier = oauth.generateRandomCodeVerifier()
  const code_challenge = await oauth.calculatePKCECodeChallenge(code_verifier)
  const code_challenge_method = 'S256'

  {
    // redirect user to as.authorization_endpoint

    const authorizationUrl = new URL(as.authorization_endpoint!)
    authorizationUrl.searchParams.set('client_id', client.client_id)
    authorizationUrl.searchParams.set('code_challenge', code_challenge)
    authorizationUrl.searchParams.set('code_challenge_method', code_challenge_method)
    authorizationUrl.searchParams.set('redirect_uri', redirect_uri)
    authorizationUrl.searchParams.set('response_type', 'code')
    authorizationUrl.searchParams.set('scope', 'openid email')
    console.log(authorizationUrl)
  }
}

doit();