package saros.ui.expressions;

import org.eclipse.core.expressions.PropertyTester;
import saros.session.User;

/** Adds test to {@link User} instances. */
public class UserPropertyTester extends PropertyTester {

  @Override
  public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {

    if (!(receiver instanceof User)) return false;

    final User user = (User) receiver;

    if ("hasWriteAccess".equals(property)) return user.hasWriteAccess();

    if ("isRemote".equals(property)) return user.isRemote();

    return false;
  }
}
