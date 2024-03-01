# Contributing to LeitnerSystem

First off, thank you for considering contributing to LeitnerSystem. It's people like you that make LeitnerSystem such a great tool.

### Our Development Process

We use Git Flow as our branching model. The base branch is `develop`. When you're ready to contribute, branch off `develop` and use the appropriate prefix for your branch (`feat`, `fix`, `chore`, `doc`), depending on the nature of your contribution.

### Git Commit Messages

Use the following format for commit messages:

```
<type>(<scope>): <subject>
```

Where `<type>`, `<scope>`, and `<subject>` are defined as follows:

- **type**: `feat`, `fix`, `chore`, `doc`
  - `feat` - new feature for the user, not a new feature for build script
  - `fix` - bug fix for the user, not a fix to a build script
  - `chore` - updating grunt tasks etc; no production code change
  - `doc` - changes to the documentation
- **scope**: The scope could be anything specifying place of the commit change. For example `login`, `quizEngine`, `userProfile`, etc.
- **subject**: The subject contains a succinct description of the change:
  - use the imperative, present tense: "change" not "changed" nor "changes"
  - don't capitalize the first letter
  - no dot (.) at the end

### Pull Requests

1. Fork the project, clone your fork, and configure the remotes:

   ```bash
   # Clone your fork of the repo into the current directory
   git clone https://github.com/jabibamman/leitner-system
   # Navigate to the newly cloned directory
   cd leitner-system
   # Assign the original repo to a remote called "upstream"
   git remote add upstream https://github.com/jabibamman/leitner-system
   ```

2. If you cloned a while ago, get the latest changes from upstream:

   ```bash
   git checkout develop
   git pull upstream develop
   ```

3. Create a new topic branch (off the `develop` branch) to contain your feature, change, or fix:

   ```bash
   git checkout -b <type>/<short-description>
   ```

4. Commit your changes in logical chunks. Use Git's interactive rebase feature to tidy up your commits before making them public.

5. Locally merge (or rebase) the upstream develop branch into your topic branch:

   ```bash
   git pull [--rebase] upstream develop
   ```

6. Push your topic branch up to your fork:

   ```bash
   git push origin <type>/<short-description>
   ```

7. Open a Pull Request with a clear title and description against the develop branch.

**IMPORTANT**: By submitting a patch, you agree to allow the project owner to license your work under the same license as that used by the project.

## Additional Notes

- Please adhere to the [Java Coding Standards](https://www.oracle.com/java/technologies/javase/codeconventions-introduction.html) for this project.
- Ensure all tests pass before submitting a pull request.
- If adding new feature or fixing a bug, please add or update the tests to verify the changes.
- Update the documentation accordingly, especially if changes affect how the project is setup or run.

Thank you for your contributions!
