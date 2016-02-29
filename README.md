# Project 4 - Twitter Client with Fragments

**Name of your app** is an android app that allows a user to view home and mentions timelines, view user profiles with user timelines, as well as compose and post a new tweet. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: 18 hours spent in total

## User Stories

The following **required** functionality is completed:

* The app includes **all required user stories** from Week 3 Twitter Client
* User can **switch between Timeline and Mention views using tabs**
  * User can view their home timeline tweets.
  * User can view the recent mentions of their username.
* User can navigate to **view their own profile**
  * User can see picture, tagline, # of followers, # of following, and tweets on their profile.
* User can **click on the profile image** in any tweet to see **another user's** profile.
 * User can see picture, tagline, # of followers, # of following, and tweets of clicked user.
 * Profile view includes that user's timeline
* User can [infinitely paginate](http://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews-and-RecyclerView) any of these timelines (home, mentions, user) by scrolling to the bottom

The following **optional** features are implemented:

* Implements robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
* When a network request is sent, user sees an [indeterminate progress indicator](http://guides.codepath.com/android/Handling-ProgressBars#progress-within-actionbar)
* User can **"reply" to any tweet on their home timeline**
  * The user that wrote the original tweet is automatically "@" replied in compose
* User can click on a tweet to be **taken to a "detail view"** of that tweet
 * User can take favorite (and unfavorite) or retweet actions on a tweet
* Improve the user interface and theme the app to feel twitter branded


The following **bonus** features are implemented:

* Use Parcelable instead of Serializable using the popular [Parceler library](http://guides.codepath.com/android/Using-Parceler).
* Apply the popular [Butterknife annotation library](http://guides.codepath.com/android/Reducing-View-Boilerplate-with-Butterknife) to reduce view boilerplate.

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='Twitter_2.gif' title='Video Walkthrough, part 1' width='' alt='Video Walkthrough' />
<img src='Twitter_2_1.gif' title='Video Walkthrough, part 2' width='' alt='Video Walkthrough' />
<img src='Twitter_2_2.gif' title='Video Walkthrough, part 3' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android

## License

    Copyright 2016 Ishan Pande

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.