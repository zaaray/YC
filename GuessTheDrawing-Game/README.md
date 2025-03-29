# Guess the drawing game using HTML, CSS, JS, Node.js, Express.js ,Socket.io
Node Modules to install before running this:
npm install

Game Instructions:
Run with node index.js 

Login with a username (index.html is the homepage)
Wait for the second player to login (waitingPage.html)

This game is a 2 player game 

The first player who logs in will be directed to the draw.html page (they are the drawer first)
the second player who logs in will be directed to the diplay.html page (they are guessing what word the drawing is supposed to depict)

Each round is 45 seconds. 
The timer starts once the drawers selects a category 
Based on the category selection, a random word will be displayed in the timer/score/round div (pinkish box)

If the guesser is able to guess the word correctly, the time remaining on the clock will be added to their co-op score 

The two players should work together to get the highest score possible within the 4 rounds 
This incentivizes the drawer to do their best with their drawing and the guesser to try their best to guess correctly. 

At the end of each 45-second round, the guesser and drawer will "switch roles"
the guesser is redirected to the drawing page (draw.html), and the drawer is redirected the to the guessing page (display.html). 

At the end of 4 rounds, the overall score for the game is displayed, and both players are redirected to the homepage to play again.

# Some Cool Features:
1. Profanity Filter
2. Close Guess Flagging using the Levenshtein Distance Algorithm
3. Synonym Checking using Merriam-Webster Dictionary API

![img1](https://github.com/aaq225/GuessTheDrawing/assets/123427105/b619b8ad-1640-4222-9332-e9043c727357)
![img2](https://github.com/aaq225/GuessTheDrawing/assets/123427105/a0ab7801-30ab-4e5d-a7ba-2b7baaa6c9d2)
![img3](https://github.com/aaq225/GuessTheDrawing/assets/123427105/16bf5313-aa62-4f3c-a82f-df7f9910f348)
![img4](https://github.com/aaq225/GuessTheDrawing/assets/123427105/7224124a-fa17-43c5-bd46-6c0fd44faf55)
![img5](https://github.com/aaq225/GuessTheDrawing/assets/123427105/47c1c7d9-55c0-44af-8adc-8825d53fba56)


