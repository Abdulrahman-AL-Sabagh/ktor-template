import org.jetbrains.annotations.TestOnly

data class Config(
    val databaseUser: String,
    val databasePassword: String,
    val dataBaseName: String,
    val databaseUrl: String
) {
    companion object {

        @TestOnly
        fun forTest() = Config(
            "postgres",
            "postgres",
            "postgres",
            "jdbc:postgresql://localhost:5433/postgres"
        )
    }
}
