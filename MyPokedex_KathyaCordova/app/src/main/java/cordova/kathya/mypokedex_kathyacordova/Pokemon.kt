package cordova.kathya.mypokedex_kathyacordova

data class Pokemon(
    var nombrePokemon: String = "",
    var numPokedex: String = "",
    var imgPokemon: String = ""
) {
    constructor() : this("", "", "")
}
