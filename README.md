# Quizleaderboard system

## Problem
External API Poll 10 times, deal with duplicate instances, calculate points and set up leaderboards.

## How it works
- Polls /quiz/message 10 times with a five second delay between each
- Duplicate events using roundId + player as a separate key
- Overall point-by-point rankings by participants
- Sorts the leaderboard by total score
- Final leaderboard is submitted once in /quiz/set up

## How to run
1. Make sure Java is mounted
2. Compile: javac QuizManagerTable.Java
3. Run: java Quiz Leaderboard

## Registration
RA2311050010018
