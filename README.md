# Quizleaderboard system

## Problem

Build an application that checks the external Q&A API 10 times, processes the replay responses, collects scores tailored to players, and generates accurate leaderboards

## How it works

1. Checks `GET /query/message` 10 instances (check zero–9) with mandatory five-2d breaks between each request
2. API collects all event data from responses
Three of them. Duplicate events using `roundId + participant` as composite key
4. Cumulative overall rankings for each player
Five. Sorts the leaderboard in descending order by overall rating
6. Calculates the composite joint score across all members
7. Submits the final leaderboard once through `POST /quiz/submit-------------the ballot =<zero-nine>` options for a particular vote
submit` The very last

## Double Handling

Similar opportunity data appear on several choices. To avoid inflated ratings, each occasion is uniquely verified using `roundId + player`. If a duplicate is found, it is dropped.

## How to run

1. Make sure Java is installed (JDK eleven+).
2. Assembly:
   Runs
   javac QuizManagerTable.java
   ```
3. Run:
   Runs
   java Quiz Table
   ```

## Registration Number

RA2311050010018
