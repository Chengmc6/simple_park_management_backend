# simple_park_management_backend

駐車場管理システム（Simple Park Management System）

このプロジェクトは、Spring Boot を使用して構築されたシンプルな駐車場管理システムです。ユーザーの登録・認証、車両情報の管理、および駐車場の利用履歴を効率的に管理するための RESTful API を提供します。複数のユーザーが複数の車両を共有利用する環境に対応しており、各利用者のアルコールチェックなどの安全管理機能も組み込まれています。

本プロジェクトは、Java / Spring Boot を用いた
実務レベルの Web API 設計・実装力の習得を目的として作成しました。

## 設計意図（設計方針）

本プロジェクトでは、実務での保守性・可読性を意識し、以下の方針で設計・実装を行いました。

- Controller 層ではリクエストの受付とレスポンス制御のみを担当し、
  ビジネスロジックは Service 層に集約
- DTO と Entity を分離し、MapStruct を用いて変換処理を統一
- 認証・認可処理は Spring Security に集約し、
  業務ロジックとセキュリティ処理を分離
- レスポンス形式を統一し、フロントエンドとの連携を容易に設計
- 包括的な例外処理とエラーハンドリングを実装し、ユーザーに分かりやすいエラーメッセージを提供

## 主な機能

✅ **ユーザー管理**
- ユーザーの新規登録
- ユーザー認証（JWT トークンベース）
- ユーザー情報の管理
- パスワード変更機能
- 管理者とユーザーのロール分離

✅ **車両管理**
- 車両情報の登録
- 車両情報の編集・更新
- 車両情報の削除
- 車両の使用状況管理（使用可能/乗車中の状態管理）

✅ **利用履歴管理**
- 乗車時間と降車時間の記録
- 乗車時と降車時のアルコール度数の記録
- 利用者ごとの履歴閲覧
- ページング対応の履歴一覧取得

✅ **セキュリティ機能**
- Spring Security による認証・認可
- JWT トークンベースの API 認証
- リクエスト/レスポンスの統一形式
- 包括的な例外処理とエラーハンドリング

✅ **アーキテクチャ設計**
- DTO/Entity の分離による保守性の向上
- MapStruct によるオブジェクト変換
- MyBatis-Plus による ORM 処理
- REST API の標準化

## 技術スタック

| 技術 | バージョン | 用途 |
|------|----------|------|
| Java | 17 | プログラミング言語 |
| Spring Boot | 3.4.10 | フレームワーク |
| Spring Security | 6.x | セキュリティ・認証 |
| MyBatis-Plus | - | ORM フレームワーク |
| MapStruct | - | オブジェクト変換 |
| MySQL/MariaDB | - | データベース |
| Maven | - | ビルド管理ツール |

## プロジェクト構成

```
src/
├── main/
│   ├── java/com/example/park/
│   │   ├── ParkApplication.java
│   │   ├── controller/
│   │   │   ├── UserController.java          - ユーザーAPI
│   │   │   ├── CarController.java           - 車両API
│   │   │   └── CarUsageController.java      - 利用履歴API
│   │   │
│   │   ├── domain/
│   │   │   ├── entity/
│   │   │   │   ├── User.java               - ユーザーエンティティ
│   │   │   │   ├── Car.java                - 車両エンティティ
│   │   │   │   └── CarUsage.java           - 利用履歴エンティティ
│   │   │   │
│   │   │   ├── dto/
│   │   │   │   ├── UserLoginRequestDTO.java
│   │   │   │   ├── UserRegisterDTO.java
│   │   │   │   ├── CarAddRequestDTO.java
│   │   │   │   ├── CarQueryRequestDTO.java
│   │   │   │   ├── UsageRequestDTO.java
│   │   │   │   └── ...
│   │   │   │
│   │   │   ├── mapper/
│   │   │   │   ├── UserMapper.java
│   │   │   │   ├── CarMapper.java
│   │   │   │   └── CarUsageMapper.java
│   │   │   │
│   │   │   ├── service/
│   │   │   │   ├── UserService.java
│   │   │   │   ├── CarService.java
│   │   │   │   ├── CarUsageService.java
│   │   │   │   └── impl/
│   │   │   │
│   │   │   └── converter/
│   │   │       └── StructMapper.java
│   │   │
│   │   ├── common/
│   │   │   ├── ApiResponse.java            - 統一レスポンス形式
│   │   │   ├── ResultCode.java             - ステータスコード
│   │   │   ├── SystemException.java        - カスタム例外
│   │   │   ├── JwtUtils.java               - JWT 処理
│   │   │   ├── JwtAuthenticationFilter.java - JWT フィルター
│   │   │   ├── AuthenticationService.java  - 認証サービス
│   │   │   ├── SecurityConfig.java         - セキュリティ設定
│   │   │   ├── PageUtils.java              - ページング処理
│   │   │   └── UserRole.java               - ロール定義
│   │   │
│   │   └── advice/
│   │       ├── GlobalExceptionHandler.java - 例外ハンドラー
│   │       └── ObjectFieldHandle.java      - フィールド処理
│   │
│   └── resources/
│       ├── application.properties          - 設定ファイル
│       ├── mapper/
│       │   ├── UserMapper.xml
│       │   ├── CarMapper.xml
│       │   └── CarUsageMapper.xml
│       ├── static/
│       └── templates/
│
└── test/
    └── java/com/example/park/
        ├── ParkApplicationTests.java
        └── CodeGenerator.java              - コード生成ユーティリティ
```

## セットアップ方法

### 前提条件

- Java 17 以上
- Maven 3.6.0 以上
- MySQL 5.7 以上（または互換性のある RDBMS）
- Git

### インストール手順

#### 1. リポジトリをクローン

```bash
git clone https://github.com/Chengmc6/simple_park_management_backend.git
cd simple_park_management_backend
```

#### 2. データベースを初期化

`docs/schema.sql` ファイルにテーブル定義が含まれています。MySQL コマンドラインまたは管理ツール（phpMyAdmin など）で実行してください。

```bash
mysql -u root -p < docs/schema.sql
```

#### 3. 設定ファイルを編集

`src/main/resources/application.properties` でデータベース接続情報を設定します。

```properties
# データベース設定
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name
spring.datasource.username=root
spring.datasource.password=your_password

# JWT 設定
app.jwtSecret=your_jwt_secret_key
app.jwtExpirationMs=86400000
```

#### 4. 依存関係をインストール

```bash
mvn clean install
```

#### 5. アプリケーション起動

```bash
mvn spring-boot:run
```

または IDE で `ParkApplication.java` を実行します。

デフォルトではアプリケーションは `http://localhost:8080` で起動します。

## API エンドポイント

### ユーザー関連 API

| メソッド | エンドポイント | 説明 |
|---------|-------------|------|
| POST | `/park/user/register` | ユーザー登録 |
| POST | `/park/user/login` | ユーザーログイン |
| GET | `/park/user/info` | ログインユーザー情報取得 |
| PUT | `/park/user/info` | ユーザー情報更新 |
| POST | `/park/user/password-change` | パスワード変更 |

### 車両関連 API

| メソッド | エンドポイント | 説明 |
|---------|-------------|------|
| POST | `/park/car/add` | 車両登録 |
| GET | `/park/car/list` | 車両一覧取得（ページング対応） |
| GET | `/park/car/{id}` | 車両詳細取得 |
| PUT | `/park/car/update` | 車両情報更新 |
| DELETE | `/park/car/delete` | 車両削除 |

### 利用履歴関連 API

| メソッド | エンドポイント | 説明 |
|---------|-------------|------|
| POST | `/park/usage/ride` | 乗車開始 |
| POST | `/park/usage/drop` | 降車処理 |
| GET | `/park/usage/list` | 利用履歴一覧取得 |
| GET | `/park/usage/{id}` | 利用履歴詳細取得 |

## フロントエンドとの連携

本バックエンドは、以下のフロントエンドリポジトリと連携して動作します。

- Frontend Repository  
  https://github.com/Chengmc6/simple_park_management_frontend

JWT 認証を用いた SPA 構成で、ログイン後は Authorization ヘッダーを通じて
API へアクセスします。

## 使用例

### 1. ユーザー登録

```bash
curl -X POST http://localhost:8080/park/user/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "taro",
    "password": "password123"
  }'
```

### 2. ログイン

```bash
curl -X POST http://localhost:8080/park/user/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "taro",
    "password": "password123"
  }'
```

レスポンスから `token` を取得し、以下のリクエストで使用します。

### 3. 車両登録

```bash
curl -X POST http://localhost:8080/park/car/add \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "carNumber": "ABC-1234"
  }'
```

### 4. 乗車開始

```bash
curl -X POST http://localhost:8080/park/usage/ride \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {token}" \
  -d '{
    "carId": 1,
    "rideAlcoholLevel": 0.0
  }'
```

### 5. 車両一覧取得

```bash
curl -X GET "http://localhost:8080/park/car/list?pageNum=1&pageSize=10" \
  -H "Authorization: Bearer {token}"
```

## レスポンス形式

すべての API は以下の統一形式でレスポンスを返します。

### 成功時

```json
{
  "code": 200,
  "message": "success",
  "data": {
    // レスポンスデータ
  }
}
```

### エラー時

```json
{
  "code": 400,
  "message": "エラーメッセージ",
  "data": null
}
```

## エラーコード

| コード | 説明 |
|------|------|
| 200 | 成功 |
| 400 | リクエストエラー |
| 401 | 認証エラー |
| 403 | 権限不足 |
| 404 | リソースが見つかりません |
| 500 | サーバーエラー |

## 開発に関するアドバイス

### ローカル開発での作業フロー

1. **ブランチ作成**
   ```bash
   git checkout -b feature/new-feature
   ```

2. **コード作成・テスト**
   ```bash
   mvn test
   ```

3. **ビルド確認**
   ```bash
   mvn clean package
   ```

4. **コミット・プッシュ**
   ```bash
   git add .
   git commit -m "feat: 新機能を追加"
   git push origin feature/new-feature
   ```

5. **プルリクエスト作成**

### コードスタイル

- Java: Google Java Style Guide に準拠
- ネーミング: camelCase を使用
- コメント: 日本語で記述

## トラブルシューティング

### 1. MySQL 接続エラー

```
Could not connect to localhost:3306
```

**解決策：**
- MySQL サーバーが起動しているか確認
- `application.properties` のホスト、ユーザー、パスワードが正しいか確認

### 2. JWT トークンの検証エラー

```
Invalid JWT token
```

**解決策：**
- `application.properties` の `app.jwtSecret` が正しく設定されているか確認
- トークンの有効期限を確認

### 3. テーブルが見つからない

```
Table 'database.user' doesn't exist
```

**解決策：**
- `docs/schema.sql` を実行してテーブルを作成

## ライセンス

このプロジェクトは MIT ライセンスのもとで公開されています。詳細は LICENSE ファイルをご覧ください。

## 作者

**GAOMING**
- Java / Spring Boot を中心に学習中
- バックエンド開発を志望
- GitHub: [Chengmc6](https://github.com/Chengmc6)

## 改版履歴

- **v0.0.1** (2025-12-20) - 初版リリース

## サポート

問題が発生した場合は、以下をご覧ください：
- [GitHub Issues](https://github.com/Chengmc6/simple_park_management/issues)
- [HELP.md](./HELP.md)
