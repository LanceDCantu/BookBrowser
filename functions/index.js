
var request = require('request');
var parseString = require('xml2js').parseString;
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();



exports.bookInfo = functions.https.onRequest((req, res) => {
    const isbn = req.query.isbn;
    console.log('bookInfo', isbn);
    const goodreadsUrl = functions.config().goodreads.url;
    const goodreadsKey = functions.config().goodreads.key;
    const url = goodreadsUrl + '/book/isbn/' + isbn +'?key=' + goodreadsKey;
    console.log('url is ' + url);
    request(url, (error, response, body) => {
        if (!error && response.statusCode === 200) {
              console.log('received ' + body);
              var result = parseXML(body);
              res.statusCode = 200;
              res.set('Content-Type', 'application/json');
              console.log('Result', JSON.stringify(result));
              res.end(JSON.stringify(result));
        } else {
              console.log('went wrong!!', error);
        }

        });

});

function parseXML(data) {
  var result = {};
  parseString(data, (err, xml) => {
    if(err) throw err;
    console.log('object:', JSON.stringify(xml));

    const book = xml.GoodreadsResponse.book[0]
    result.title = book.title[0];
    result.bookImageURL = book.image_url[0];
    result.description = book.description[0];
    result.reviews = book.reviews_widget[0];

  });
  return result;
}



