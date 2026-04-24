# Quiz Leaderboard

## What this does

This is a Java program that polls a quiz API 10 times, removes duplicate entries, adds up scores for each participant, and submits the final leaderboard.

## How it works

- Calls `GET /quiz/messages` with poll values from 0 to 9
- Waits 5 seconds between each poll (as required)
- Some events repeat across polls, so I used `roundId + participant` as a key to skip duplicates
- After collecting everything, scores are totalled per participant
- Leaderboard is sorted highest score first
- Submitted once to `POST /quiz/submit`

## Running it

You need Java 11 or above.

```bash
javac QuizLeaderboard.java
java QuizLeaderboard
Registration No
RA2311050010018
