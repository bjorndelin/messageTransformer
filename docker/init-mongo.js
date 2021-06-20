db.createUser(
    {
        user: "user",
        password: "password",
        roles: [
            {
                role: "readWrite",
                db: "messageTransformer"
            }
        ]
    }
)