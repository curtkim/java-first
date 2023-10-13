let crypto;
try {
  crypto = require('node:crypto');
} catch (err) {
  console.error('crypto support is disabled!');
} 

function base64URLEncode(str) {
    return str.toString('base64')
        .replace(/\+/g, '-')
        .replace(/\//g, '_')
        .replace(/=/g, '');
}
var body = 'my-string' 
//var body = crypto.randomBytes(32)
var verifier = base64URLEncode(body);

function sha256(buffer) {
    return crypto.createHash('sha256').update(buffer).digest();
}
var challenge = base64URLEncode(sha256(verifier));

console.log(verifier);
console.log(challenge);
console.log(sha256(verifier));
