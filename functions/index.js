
// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
// import the module
var request = require('request');

var xml2js = require('xml2js');
const functions = require('firebase-functions');


const admin = require('firebase-admin');
admin.initializeApp();

exports.bookInfo = functions.https.onRequest((req, res) => {
    const isbn = req.query.isbn;
    return admin.database().ref('/books').push({isbn: isbn}).then((snapshot) => {
        // Redirect with 303 SEE OTHER to the URL of the pushed object in the Firebase console.
        return res.redirect(303, snapshot.ref.toString());
      });
});

exports.parseXML = (req, res) => {
    //Convert the request to a Buffer and a string
    //Use whichever one is accepted by your XML parser
    let data = req.rawBody;
    let xmlData = data.toString();

    const parseString = require('xml2js').parseString
}

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
              var extractedData = "";
              var parser = new xml2js.Parser();
              parser.parseString(body, function(err, result){
                if(err)
                {
                    console.error(err);
                    return;
                }
                //Extract the value from the data element
                extractedData = result;
                console.log(extractedData);
              });


              return snapshot.ref.parent.child('bookInfo').set(extractedData);
              //return snapshot.ref.parent.child('bookInfo').set(body);
              //snapshot.ref.parent.child('bookInfo').set(body);
          }
          //return snapshot.ref.parent.child('status-code').set(response.statusCode);
          else {
            console.log('status code = ' + response.statusCode);
          }
      });
      return snapshot.ref.parent;
    });






