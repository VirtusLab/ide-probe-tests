package com.example

import org.junit.runner.RunWith
import org.scalatest.WordSpec
import org.scalatest.junit.JUnitRunner
import org.scalatest.MustMatchers

@RunWith(classOf[JUnitRunner])
class Addition extends WordSpec with MustMatchers {
  "1 + 1" should {
    "equal 2" in {
      (1 + 1) mustEqual 2
    }
    "equal 3" in {
      (1 + 1) mustEqual 3
    }
  }
}
