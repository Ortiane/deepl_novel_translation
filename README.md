# deepl_novel_translation
The application will scrap text files from dropbox and use deepl's translator API to translate the text files to english.

- To use, set environment variables for the API keys for dropbox (ACCESS_TOKEN) and deepl (DEEPL_AUTH_KEY).
- Then call App.java with the arguments: ["DROPBOX_DIRECTORY" "LOCAL_SAVE_DIRECTORY"]

DeepL only allows for translation of 500k characters for free tier users then ends up costing $25 per 1 million characters which is very much not worth it. 

Get a free trial for a standard non-API user in deepL and send the documents in by hand. It will have a single month cost.
