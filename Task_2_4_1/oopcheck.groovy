importConfig "tasks.groovy"
importConfig "groups.groovy"

check {
    group "22214"
    task "Task_2_1_1"
    task "Task_2_2_1"
}

submissions {
    submitted {
        student "example-student"
        task "Task_2_1_1"
        softPassed true
        hardPassed true
    }

    submitted {
        student "example-student"
        task "Task_2_2_1"
        softPassed false
        hardPassed true
    }
}

checkpoints {
    checkpoint("КТ-1") {
        date "2025-03-10"
    }

    checkpoint("КТ-2") {
        date "2025-05-20"
    }
}

settings {
    workDir "build/checker-work"
    commandTimeoutSeconds 120

    grade "отлично", 13
    grade "хорошо", 10
    grade "удовлетворительно", 7

    bonus {
        student "example-student"
        task "Task_2_1_1"
        points 1.0
        reason "Пятиминутка"
    }
}