# MorraGame
A digital hand game demonstrating basic threading and TCP networking, a project for CS342: Software Design.

This was a group project. My partner and I were in frequent communication, but they were working on a Macbook and spent nearly the entire time setting up their Eclipse environment and were extremely busy with midterms, so the majority of the workload was left for me to complete. Not fun!!

This was a very difficult project, compared to what we had been assigned previously. Having a two-person workload to myself meant that the pressure was on. I took a weekend to just rest after the deadline, I remember starting Breaking Bad the night I turned it in and spending the entire weekend just watching that and recuperating.

The project is a simple networked two-player game. When a server has been launched with JavaFX, two clients can join it at its selected port (and the localhost ip, as we had not talked about online networking yet). When two players have joined, the game begins.

How to play:

Each player selects a number of fingers to play, and a number of total fingers to guess. Iff one of them guesses the total number of displayed fingers from both parties, they score a point. Otherwise, the game continues to the next round. Once one player scores two points, they win and the players are asked to play again. If one quits, the other player is notified and the game is over.

The server window tracks all of this. As the game is played, the server is responsible for all calculations, reading both players' inputs, and updating both client instances with the new state of the game.


Things I learned from this project:

1. Networking is hard! We weren't required to consider all edge cases in this, like what happens to the remaining player once one quits, or what happens to both players if the server goes down, and I expect that I'd have completely lost my mind if I had to account for those cases on my own.
2. Communicate with potential collaborators before(!!!) deciding a partner. I'm not sure how my partner had not set up his environment properly by the fifth week of class, and he was only available on very specific days for small periods of time to troubleshoot this, much less get working on the assignment. What seemed to be a pretty fun project for my classmates was a test of patience and endurance for me.
3. Don't use SceneBuilder as a crutch! For two days, we struggled greatly with JavaFX SceneBuilder's generated FXML files. The objects they defined for the scene were messing up in all sorts of confusing ways that we couldn't troubleshoot. A TA came in clutch when he realized that the FXML file itself had come out deformed and that the FX objects had been improperly defined there. The entire time, I had been pretty hard on myself for making some enormous mistake, but in reality, it was neither my or my partner's code that was causing so much trouble. I've got to be nicer to myself!
4. FXML is really useful (when it DOES work!). Development in Java is really interesting and definitely seems more conducive to pumping out an actual working product that's good to look at. I enjoyed making the UI!



Demonstration:

![1](https://user-images.githubusercontent.com/113747039/193122810-e6907bd0-7184-492b-9432-3381a4ab6616.png)

Server opened on port 5555

![2](https://user-images.githubusercontent.com/113747039/193122858-7479e952-7066-4e67-aa6a-536045415f87.png)

Two clients connect at local port 5555

![3](https://user-images.githubusercontent.com/113747039/193122901-64604d56-fd84-487a-b56b-47c673dcd470.png)

Two clients and the server console running side-by-side

![4](https://user-images.githubusercontent.com/113747039/193122965-e2cf2624-37a2-4f16-94ab-fac82c7d15f9.png)

Two clients begin playing, selecting their input. In this scenario, player one will score a point.

![5](https://user-images.githubusercontent.com/113747039/193123064-07c409dc-503d-4ab7-ad75-a5ec91ceabba.png)

Both players have submitted, player one scores.

![6](https://user-images.githubusercontent.com/113747039/193123135-a7cb52cd-4c81-4da2-bd47-272e5b88b840.png)

Player one has submitted their next move. Their input is disabled until player two has played too.

![7](https://user-images.githubusercontent.com/113747039/193123252-3f65edb9-13a5-4e6d-b6db-f1ef79c13e19.png)

Player two has played. Player one has now scored two points, making them the winner. Both clients are asked if they would like to play again.

![8](https://user-images.githubusercontent.com/113747039/193123377-65a127cc-c9b5-4161-960b-8c91f6d82240.png)

Both players would like to play another game. Scores are reset and the screens are reloaded to their initial values.

