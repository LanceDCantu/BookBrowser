
const request = require('request-promise-native');
const parseString = require('xml2js').parseString;
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();


exports.bookInfov2 = functions.https.onCall((data) => {
    const isbn = data.isbn;
    const goodreadsUrl = functions.config().goodreads.url;
    const goodreadsKey = functions.config().goodreads.key;
    const url = goodreadsUrl + '/book/isbn/' + isbn +'?key=' + goodreadsKey;

    return request(url).then((htmlString) => {
         var result = parseXML(htmlString);
         return result;
    });
});


//exports.bookInfo = functions.https.onCall((data) => {
//    const isbn = data.isbn;
//
//    console.log('bookInfo', isbn);
//    const goodreadsUrl = functions.config().goodreads.url;
//    const goodreadsKey = functions.config().goodreads.key;
//    const url = goodreadsUrl + '/book/isbn/' + isbn +'?key=' + goodreadsKey;
//    console.log('url is ' + url);
//    request(url, (error, response, body) => {
//        if (!error && response.statusCode === 200) {
//              console.log('received ' + body);
//              var result = parseXML(body);
//              console.log('result ' + result.title + " " + result.description);
//
//              return {
//                title: result.title,
//                isbn: isbn
//              };
//        } else {
//              console.log('went wrong!!', error);
//        }
//
//        });
//
//});

//exports.populateBookInfo = functions.database.ref('products/{isbn}/lastUpdate')
//    .onWrite((change, context) => {
//        // check to see if record has been deleted
//        if (!change.after.exists()) {
//            return null;
//        }
//
//        const isbn = change.after.ref.parent.key;
//        console.log("populateBookInfo", isbn);
//
//        const goodreadsUrl = functions.config().goodreads.url;
//        const goodreadsKey = functions.config().goodreads.key;
//        const url = goodreadsUrl + '/book/isbn/' + isbn +'?key=' + goodreadsKey;
//
//        return request(url).then((htmlString) => {
//            var result = parseXML(htmlString);
//            change.after.ref.parent.update(result);
//        });
//    }
//);

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



