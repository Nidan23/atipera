package pl.sulkowski.jakub.atipera.model

data class RepositoryModel(
    val name: String,
    val ownerName: String,
    val branches: Array<BranchModel>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RepositoryModel

        if (name != other.name) return false
        if (ownerName != other.ownerName) return false
        if (!branches.contentEquals(other.branches)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + ownerName.hashCode()
        result = 31 * result + branches.contentHashCode()
        return result
    }
}
