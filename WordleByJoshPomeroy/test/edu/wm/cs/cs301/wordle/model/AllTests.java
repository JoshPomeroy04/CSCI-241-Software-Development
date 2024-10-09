package edu.wm.cs.cs301.wordle.model;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectClasses({ WordleModelTest.class, WordleResponseTest.class })
public class AllTests {}