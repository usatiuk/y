Tady bude moje semestralka!

# Y

Due to X's popularity rapidly falling after the rebranding, the upper management had decided that only complete rewrite could help, and to be budget-efficient had decided to outsource it to junior student developers in the Czech Republic...

## Functionality

As it was evident with Twitter's 280 character limit, "less is more" is in our DNA, and if we extend it to the features that need to be in the next generation of social networking it is obvious - the less features the better!

As such, our new platform will support only these operations:

* Posting tweets
* Following other users and seeing all posts of who you're following in a chronological order
* Seeing who other users follow/are followed by
* DMing other users

Also, to prevent misuse of our platform, it is required to implement some admin tools, but, thankfully, our corporate overlord has suggested that for now a simple one that bans all users who have "Elon is bad" in posts they posted or messages they've sent is enough.

Our users are also usually of a very curious kind, so there will be implemented a client-side feature that allows to calculate and show statistics about their posts and messages: when they post/message the most in a nice histogram, which words they use the most and so on...

## Boring implementation details

Approx. data model (subject to change):
![DB](db.png)