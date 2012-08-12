// Load the http module to create an http server.
var http = require('http');
var url = require('url');

var nextGameId = 0;
var games = [];

var GUID = function() {
  return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(
      /[xy]/g,
      function(c) {
        var r = Math.random()*16|0,v=c=='x'?r:r&0x3|0x8;
        return v.toString(16);
      });
}

var sentences = [
  "the war on the united states",
  "I SEE--HER LADYSHIP'S WAITING-MAID",
  "NEVER SAID THE DORMOUSE",
  "OF THE MIDDLE OF THE SQUARE",
  "TREACLE SAID THE CATERPILLAR CONTEMPTUOUSLY",
  "HENCE THERE WAS A FINE TIME",
  "ILLUSTRATION DINING HALL IN THE CHARTERHOUSE",
  "the war on the united states",
  "i should be deceived again",
  "there are to be avoided",
  "the king had said that day",
  "in most of the time",
  "in the reign of louis xiv",
  "but i am all off colour",
  "it would be an unpleasant thing",
  "of the middle classes fig",
  "you must not disappoint your father",
  "on sexual union chapter i",
  "certainly he replied biting his lips",
  "there ain't no sense in it",
  "the price of the yellow metal",
  "men who talk well",
  "in all approximately fifteen millions",
  "the object of open pleasantry",
  "but to restore the southern states",
  "the crown and with one another",
  "oh merely a couple of case-knives",
  "the games of strength and agility",
  "i had shut the door to",
  "elizabeth listened as little as possible",
  "the question was impossible",
  "of the same werke xiijs",
  "you have said quite enough madam",
  "as a matter of talk only",
  "the embassy to achilles",
  "and who is this k",
  "the cause of troy"
];

var games = {};

var sentence_template = function(sentence) {
  return sentence.replace(/[a-zA-Z]/g, '_')
}

var GameStatus = {
  ALIVE : "ALIVE",
  DEAD : "DEAD",
  FREE : "FREE"
}

var kInitialGuesses = 3;

var replaceCharAt = function(str, idx, ch) {
  return str.substr(0, idx) + ch + str.substr(idx + 1, str.length);
}

// Configure our HTTP server to respond with Hello World to all requests.
var server = http.createServer(function (request, response) {
  console.log(request.url);
  var url_parts = url.parse(request.url, true);
  var query = url_parts.query;
  var data = {};
  if (query) {
    var queryLen = Object.keys(query).length;
    if ('code' in query) {
      if (queryLen == 3 && 'token' in query && 'guess' in query) {
        if (query.token in games) {
          var game = games[query.token];
          switch (game.status) {
            case GameStatus.ALIVE:
              if (query.guess.match(/^[a-zA-Z]$/)) {
                if (!(query.quess in game.guesses)) {
                  game.guesses[query.guess] = true;
                  if (game.sentence.match(query.guess)) {
                    for (var idx = game.sentence.indexOf(query.guess);
                      idx != -1;
                      idx = game.sentence.indexOf(query.guess, idx + 1)) {
                      game.state = replaceCharAt(
                        game.state, idx, query.guess);
                    }
                    if (-1 == game.state.indexOf('_')) {
                      game.status = GameStatus.FREE;
                    }
                  } else {
                    game.remainingGuesses--;
                    if (game.remainingGuesses < 0) {
                      game.status = GameStatus.DEAD;
                    }
                  }
                }
              }
              break;
            case GameStatus.DEAD:
              break;
            case GameStatus.FREE:
              break;
          }
          data.token = query.token;
          data.status = game.status;
          data.remainingGuesses = game.remainingGuesses;
          data.state = game.state;
        }
      } else if (queryLen == 1) {
        var token = GUID();
        var sentence = sentences[Math.floor(Math.random() * sentences.length)];
        var state = sentence_template(sentence);
        var game = {
          sentence : sentence,
          state : state,
          status : GameStatus.ALIVE,
          remainingGuesses : kInitialGuesses,
          guesses : {}
        }
        games[token] = game;
        data.token = token;
        data.status = game.status;
        data.remainingGuesses = game.remainingGuesses;
        data.state = game.state;
      }
    }
  }

  response.writeHead(200, {"Content-Type": "text/plain"});
  response.write(JSON.stringify(data));
  response.end()
});

server.listen(8000);

console.log("Server running at http://127.0.0.1:8000/");
