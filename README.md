
# News Feeds

Stay informed and in control with NewsFeeds, your go-to news companion. NewsFeeds is a feature-rich news application that combines real-time news updates with the convenience of storing your favorite articles locally.


## Features

- Up-to-the-Minute News
- Personalized Feed
- Local Database Storage
- Search and Discover
- Offline Mode


## API Reference

#### Get All items

```http
  GET /v2/everything
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `api_key` | `string` | **Required** Your API key
| `q` | `string` | **Optional** Search text
| `from` | `string` | **Optional** Starting Date
| `to` | `string` | **Optional** End Date
| `pageSize` | `string` | **Optional** Total Content
| `sortBy` | `string` | **Optional** Sort the items



#### Get Top Headlines

```http
  GET /v2/top-headlines
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `api_key` | `string` | **Required** Your API key
| `country` | `string` | **Optional** ISO3 Country Name
| `category` | `string` | **Optional** category
| `pageSize` | `string` | **Optional** Total Content

## Authors

- [@KapilKori](https://www.github.com/10-kapilkori)

