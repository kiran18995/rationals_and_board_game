package board

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)


open class SquareBoardImpl(final override val width: Int) : SquareBoard {

    private val arrayList = ArrayList<Cell>(width * width)

    init {
        (1..width).forEach {
            (1..width).forEach { itr ->
                arrayList.add(Cell(it, itr))
            }
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? = getAllCells().find { it == Cell(i, j) }

    override fun getCell(i: Int, j: Int): Cell {
        return getAllCells().find { it == Cell(i, j) } ?: throw IllegalArgumentException("Invalid Cell ($i, $j)")
    }

    override fun getAllCells(): Collection<Cell> = arrayList

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> =
        jRange.filter { it <= width }.map { itr -> getCell(i, itr) }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> =
        iRange.filter { it <= width }.map { itr -> getCell(itr, j) }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            Direction.UP -> getCellOrNull(this.i - 1, this.j)
            Direction.DOWN -> getCellOrNull(this.i + 1, this.j)
            Direction.LEFT -> getCellOrNull(this.i, this.j - 1)
            Direction.RIGHT -> getCellOrNull(this.i, this.j + 1)
        }
    }

}

class GameBoardImpl<T>(width: Int) : GameBoard<T>, SquareBoardImpl(width) {

    private val boardCells = getAllCells().associateWith { null as T? }.toMutableMap()

    override fun get(cell: Cell): T? = boardCells[cell]

    override fun set(cell: Cell, value: T?) {
        boardCells[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> = boardCells.filterValues(predicate).keys

    override fun find(predicate: (T?) -> Boolean): Cell = boardCells.filter { predicate(it.value) }.keys.first()

    override fun any(predicate: (T?) -> Boolean): Boolean = boardCells.any { predicate(it.value) }

    override fun all(predicate: (T?) -> Boolean): Boolean = boardCells.all { predicate(it.value) }

}