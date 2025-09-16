from app.tasks import factorial, is_palindrome, run_tasks, sum_numbers


def test_sum_numbers():
    assert sum_numbers([1, 2, 3]) == 6


def test_is_palindrome():
    assert is_palindrome("Ame a ema")
    assert not is_palindrome("python")


def test_factorial():
    assert factorial(0) == 1
    assert factorial(5) == 120


def test_run_tasks():
    results = run_tasks([lambda: sum_numbers([1, 1]), lambda: factorial(3)])
    assert results[0]["result"] == 2
    assert results[1]["result"] == 6
