
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


function parseXML(data) {
  var result = {};
  parseString(data, (err, xml) => {
    if(err) throw err;
    console.log('object:', JSON.stringify(xml));

    const book = xml.GoodreadsResponse.book[0]
    result.title = book.title[0];
    result.author = book.authors[0];
    result.bookImageURL = book.image_url[0];
    result.description = book.description[0];
    result.reviews = book.reviews_widget[0];

  });
  return result;
}



