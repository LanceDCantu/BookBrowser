
var request = require('request');
var parseString = require('xml2js').parseString;
const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp();

function parseXml(data)
{
    var result = {};
    parseString(data, (err, xml) => {
        if (err) throw err;
        console.dir('in parse:', JSON.stringify(xml));
        result.bookimageurl = 'https://nowhere.com';
        result.description = 'description';
        result.iframe = '<iframe/>';
    });

    return result;

}

exports.bookInfo = functions.https.onRequest((req, res) => {
    const isbn = req.query.isbn;
    console.log('bookInfo', isbn);
    const goodreadsUrl = functions.config().goodreads.url;
    const goodreadsKey = functions.config().goodreads.key;
    const url = goodreadsUrl + '/book/isbn/' + isbn +'?key=' + goodreadsKey;
    console.log('url is ' + url);
    var result = {'alpha':'beta'};
    request(url, (error, response, body) => {
        if (!error && response.statusCode === 200) {
              console.log('recieved ' + body);
              result = parseXml(body);
              console.dir('result:', JSON.stringify(result));
        } else {
              console.log('went wrong!!', error);
        }

        });
        res.writeHead(200, {'Content-Type': 'application/json'});
    res.end(result);
});






