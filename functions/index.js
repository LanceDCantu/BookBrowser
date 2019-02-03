
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
// import the module
var request = require('request');

const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp();
/*
// Take the text parameter passed to this HTTP endpoint and insert it into the
// Realtime Database under the path /messages/:pushId/original
exports.addMessage = functions.https.onRequest((req, res) => {
  // Grab the text parameter.
  const original = req.query.text;
  // Push the new message into the Realtime Database using the Firebase Admin SDK.
  return admin.database().ref('/messages').push({original: original}).then((snapshot) => {
    // Redirect with 303 SEE OTHER to the URL of the pushed object in the Firebase console.
    return res.redirect(303, snapshot.ref.toString());
  });
});

// Listens for new messages added to /messages/:pushId/original and creates an
// uppercase version of the message to /messages/:pushId/uppercase
exports.makeUppercase = functions.database.ref('/messages/{pushId}/original')
    .onCreate((snapshot, context) => {
      // Grab the current value of what was written to the Realtime Database.
      const original = snapshot.val();
      console.log('Uppercasing', context.params.pushId, original);
      const uppercase = original.toUpperCase();
      // You must return a Promise when performing asynchronous tasks inside a Functions such as
      // writing to the Firebase Realtime Database.
      // Setting an "uppercase" sibling in the Realtime Database returns a Promise.
      return snapshot.ref.parent.child('uppercase').set(uppercase);
    });
*/

exports.bookInfo = functions.https.onRequest((req, res) => {
    const isbn = req.query.isbn;
    return admin.database().ref('/books').push({isbn: isbn}).then((snapshot) => {
        // Redirect with 303 SEE OTHER to the URL of the pushed object in the Firebase console.
        return res.redirect(303, snapshot.ref.toString());
      });
});

exports.bookLookUp = functions.database.ref('/books/{pushId}/isbn')
    .onCreate((snapshot, context) => {
      // Grab the current value of what was written to the Realtime Database.
      const isbn = snapshot.val();
      console.log('bookLookUp', context.params.pushId, isbn);
      const goodreadsUrl = functions.config().goodreads.url;
      const goodreadsKey = functions.config().goodreads.key;
      const url = goodreadsUrl + '/book/isbn/' + isbn +'?key=' + goodreadsKey;
      console.log('url is ' + url);
      // You must return a Promise when performing asynchronous tasks inside a Functions such as
      // writing to the Firebase Realtime Database.
      // Setting an "uppercase" sibling in the Realtime Database returns a Promise.
      // make the request
      request(url, (error, response, body) => {
          if (!error && response.statusCode === 200) {
              console.log('recieved ' + body);
              //return snapshot.ref.parent.child('bookInfo').set(body);
              snapshot.ref.parent.child('bookInfo').set(body);
          }
          //return snapshot.ref.parent.child('status-code').set(response.statusCode);
          else {
            console.log('status code = ' + response.statusCode);
          }
      });
      return snapshot.ref.parent;
    });




