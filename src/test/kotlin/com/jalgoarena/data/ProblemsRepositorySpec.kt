package com.jalgoarena.data

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.jalgoarena.domain.Problem
import com.jalgoarena.utils.SetupProblemsStore
import com.winterbe.expekt.should
import org.junit.AfterClass
import org.junit.Test
import java.io.File


class ProblemsRepositorySpec {

    companion object {
        val dbName = "./ProblemsStoreForTests"
        var repository: ProblemsRepository

        init {
            SetupProblemsStore(dbName).createDb()
            repository = XodusProblemsRepository(dbName)
        }

        @AfterClass
        @JvmStatic fun tearDown() {
            repository.destroy()
            SetupProblemsStore(dbName).removeDb()
        }
    }

    @Test
    fun should_return_all_available_problems() {
        val problems = repository.findAll()
        problems.should.have.size.above(54)
    }

    @Test
    fun should_return_particular_problem() {
        val problem: Problem = repository.find("fib")!!
        problem.title.should.equal("Fibonacci")
    }

    @Test
    fun should_return_all_test_cases_for_particular_problem() {
        val problem: Problem = repository.find("fib")!!
        problem.testCases!!.size.should.equal(9)
    }

    @Test
    fun should_allow_on_adding_new_problem() {
        var file = File("problems.json")
        val problems = jacksonObjectMapper().readValue(
                file, Array<Problem>::class.java
        )

        System.out.println("LOL" + problems.count())
        System.out.println("EXISTS: " + file.isFile() + " CAN READ" + file)

        val fibProblem = problems.filter { it.id == "fib" }.first()

        val newTestProblem = Problem(
                "test-fib",
                "Test Title",
                fibProblem.description,
                fibProblem.timeLimit,
                fibProblem.func,
                fibProblem.testCases,
                fibProblem.level
        )

        repository.addOrUpdate(newTestProblem)

        val savedProblem = repository.find("test-fib")
        savedProblem!!.title.should.equal("Test Title")
    }
}
