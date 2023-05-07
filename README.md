## Problem
Decentralized Autonomous Organizations (DAOs) are gaining popularity in the world of blockchain and cryptocurrency. They are a form of self-governance that enables members to vote on proposals and make decisions without the need for centralized authority. However, one of the challenges faced by DAOs is low participation in voting, which can result in decision-making bottlenecks. To solve this problem,we have created VoteAPP that incentivize members to vote on proposals through rewards and badges. 

## Incentivizing Voting
The idea behind incentivizing voting is to encourage DAO members to actively participate in decision-making by providing rewards for their contributions. The rewards will come in the form of tokens, which can be used within the DAO's ecosystem . By providing incentives, the users can increase participation rates and ensure that decisions are made by a diverse group of members with different perspectives.

## Designing the App

To create an VoteApp a incentivizes voting, the following features should are included:

_1) Proposal Listings: The app display a list of proposals that are currently up for voting. Each proposal should have a brief description, its status (open/closed), and the number of votes received so far.

_2) Rewards System: The smartcontract have a system that tracks and awards tokens to members who vote on proposals. The rewards system should be transparent and accessible to all members, so they can see how many tokens they have earned and how many they need to earn to reach the next reward leve

## Designing the App
How does my application solve these problems? Artists can **upload their music as an NFT** which gives them ownership of their music which then can be **freely traded on any NFT marketplace.** They can also **attach royalties to their music** so that when ownership of the song is traded between owners, they get a portion of the revenue. The music is also **uploaded as lossless  _.wav_ files thanks to IPFS and Filecoin** providing a safe and easy way to store large files. This allows listeners to have access to the original audio that the artist intended for them to hear. The platform also **supports tipping your favorite artists with NEAR tokens,** so listeners support their favorite artists directly.

## Benefits of Incentivizing Voting

By incentivizing voting through rewards,app can provide the following benefits: 1. Increased Participation: Members are more likely to participate in decision-making when there are incentives involved. By providing rewards and recognition, the app can increase participation rates and ensure that decisions are made by a diverse group of members. 2. More Informed Decisions: When more members participate in voting, the decisions made are likely to be more informed and representative of the DAO's values and goals. 3. Better Engagement: VoteAPP can increase member engagement by providing a platform for discussion and debate around proposals. This can help to create a more cohesive and collaborative community.

For the front-end, I used next-js-api to make RPC calls from my application.

### [/nearsound/context/NearProvider.js](https://github.com/pythonicode/nearsound/blob/main/context/NearProvider.js)
```js
const _tokens = near.connection.provider.query({
   request_type: "call_function",
   finality: "final",
   account_id: "nearsound.testnet",
   method_name: "nft_tokens",
   args_base64: DEFAULT_SEARCH,
});
const _search = near.connection.provider.query({
   request_type: "call_function",
   finality: "final",
   account_id: "nearsound.testnet",
   method_name: "get_search_terms",
   args_base64: EMPTY_QUERY,
});
const _request = near.connection.provider.experimental_genesisConfig();
const [default_tokens, search_terms, response] = await Promise.all([
   _tokens,
   _search,
   _request,
]);
```

There were 5 ways that my application communicated with my smart contract.

1) Fetch data from all tokens minted on the smart contract

2) Fetch data for a certain subset of tokens on the smart contract given a search term

3) Allow users to store an "Artist Name" associated with their account on the smart contract

4) Mint songs as NFTs while supporting the necessary data for the above use cases

5) Log transaction details so minting data can be fetched and displayed in the application

You can find some of the modifications to the smart contract that I made below which intend to address the above situations.

### [/nearsound/near-contracts/nft-contract/artist.rs](https://github.com/pythonicode/nearsound/blob/main/near-contracts/nft-contract/src/artist.rs)

```rust
pub fn create_artist(&mut self, artist_name: Artist) {
        let initial_storage_usage = env::storage_usage();
        let _artist = self.artist_by_account.get(&env::predecessor_account_id());
        if let Some(_artist) = _artist {
            env::panic_str("Artist already exists for this user. You cannot change your Artist Name.");
        } else {
            self.artist_by_account.insert(&env::predecessor_account_id(), &artist_name);
        }
        let required_storage_in_bytes = env::storage_usage() - initial_storage_usage;
        refund_deposit(required_storage_in_bytes);  // Refund any excess storage
}
```

### [/nearsound/near-contracts/nft-contract/internal.rs](https://github.com/pythonicode/nearsound/blob/main/near-contracts/nft-contract/src/internal.rs)

```rust
pub(crate) fn internal_add_token_to_search(&mut self, search_term: &SearchTerm, token_id: &TokenId){
        // Get tokens for a given search term
        let mut tokens_set = self.tokens_per_search.get(search_term).unwrap_or_else(|| {
            UnorderedSet::new(
                StorageKey::TokensPerSearchInner {
                    // Get a new unique prefix for the collection
                    search_term_hash: hash_search_term(&search_term),
                }
                .try_to_vec()
                .unwrap(),
            )
        });
        // Add token to set with search term
        tokens_set.insert(token_id);
        self.tokens_per_search.insert(search_term, &tokens_set);
}
```
### [/nearsound/near-contracts/nft-contract/enumeration.rs](https://github.com/pythonicode/nearsound/blob/main/near-contracts/nft-contract/src/enumeration.rs)

```rust
pub fn nft_tokens_for_search(&self, search_term: SearchTerm, from_index: Option<U128>, limit: Option<u64>) -> Vec<JsonToken> {

        let tokens_for_search = self.tokens_per_search.get(&search_term); // Get the set of tokens for the search term
        let tokens = if let Some(tokens_for_search) = tokens_for_search { // If there is tokens, we'll set the tokens variable
            tokens_for_search
        } else {
            // If there is no tokens, we'll simply return an empty vector. 
            return vec![];
        };
        let keys = tokens.as_vector(); // Convert the UnorderedSet into a vector of strings
        let start = u128::from(from_index.unwrap_or(U128(0))); // Where to start pagination
        keys.iter().skip(start as usize).take(limit.unwrap_or(0) as usize).map(|token_id| self.nft_token(token_id.clone()).unwrap()).collect()
    }
```

Above are only some of the features of the smart contract I currently have deployed at `nearsound.testnet` which acts as the provider for testnet.nearsound.org

## Challenges

Almost everything about this project was challenging! I had to...

1) Learn how to _code in Rust_

2) Learn how to _write NEAR smart contracts_

3) Learn how to _use Next JS_ for the front-end application

AND I had to put all of that together to create a final, working product.

The biggest technical challenge of the project was creating the __custom player component__ from scratch. It implements the logic that allows you to play, queue, skip, and shuffle songs from the dashboard and there are still a few kinks I have to work out.

The biggest challenge with the smart contract was modifying the contract to allow the enumeration of tokens by a given search term.

## Accomplishments

I'm proud to have started with no experience in Rust or coding NEAR smart contracts to having deployed a live application on the testnet within the span of just over 1 month!

## What's Next

This is the most exciting part! I have so much more I want to do with Nearsound, this was just the beginning. I could only do so much within 1 month so there's a lot more coming.

Here's a little roadmap of what I have planned.

1) Recreate the front-end in Svelte Kit while working out bugs and modifying the UI making it more compatible and responsive for all kinds of devices.

2) Add new features to the smart contract for tracking song plays/interactions to better display songs on the main page of the application.

3) Perhaps include a decentralized advertising ecosystem (not sure about this one)

4) Update the front-end to allow querying pages for individual artists/songs.

5) Create a marketplace contract where users can buy/sell and trade their music.

6) Deploy everything to the mainnet! 

Thanks! I hope you enjoyed my project. -Anthony
