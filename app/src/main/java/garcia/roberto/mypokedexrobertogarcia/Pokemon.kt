package garcia.roberto.mypokedexrobertogarcia

data class Pokemon(
    val id : String = "",
    val name: String = "",
    val number: Int = 0,
    val imageUrl : String = ""
) {
    constructor(name: String, number: Int, imageUrl: String) : this(
        "",
        name,
        number,
        imageUrl
    )
}
