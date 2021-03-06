/**
 * This file is part of mycollab-web.
 *
 * mycollab-web is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * mycollab-web is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with mycollab-web.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.esofthead.mycollab.module.user.accountsettings.team.view;

import com.esofthead.mycollab.core.utils.RandomPasswordGenerator;
import com.esofthead.mycollab.eventmanager.EventBusFactory;
import com.esofthead.mycollab.module.billing.UserStatusConstants;
import com.esofthead.mycollab.module.user.accountsettings.view.AccountSettingBreadcrumb;
import com.esofthead.mycollab.module.user.domain.SimpleUser;
import com.esofthead.mycollab.module.user.events.UserEvent;
import com.esofthead.mycollab.module.user.service.UserService;
import com.esofthead.mycollab.security.AccessPermissionFlag;
import com.esofthead.mycollab.security.RolePermissionCollections;
import com.esofthead.mycollab.spring.AppContextUtil;
import com.esofthead.mycollab.vaadin.AppContext;
import com.esofthead.mycollab.vaadin.events.DefaultEditFormHandler;
import com.esofthead.mycollab.vaadin.mvp.ScreenData;
import com.esofthead.mycollab.vaadin.mvp.ViewManager;
import com.esofthead.mycollab.vaadin.mvp.ViewPermission;
import com.esofthead.mycollab.vaadin.web.ui.AbstractPresenter;
import com.vaadin.server.Page;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.UI;

/**
 * @author MyCollab Ltd.
 * @since 1.0
 */
@ViewPermission(permissionId = RolePermissionCollections.ACCOUNT_USER, impliedPermissionVal = AccessPermissionFlag.READ_WRITE)
public class UserAddPresenter extends AbstractPresenter<UserAddView> {
    private static final long serialVersionUID = 1L;

    public UserAddPresenter() {
        super(UserAddView.class);
    }

    @Override
    protected void postInitView() {
        view.getEditFormHandlers().addFormHandler(new DefaultEditFormHandler<SimpleUser>() {
            private static final long serialVersionUID = 1L;

            @Override
            public void onSave(final SimpleUser item) {
                save(item);
            }

            @Override
            public void onCancel() {
                EventBusFactory.getInstance().post(new UserEvent.GotoList(this, null));
            }
        });
    }

    private void save(SimpleUser user) {
        boolean isRefreshable = false;
        if (user.getUsername() != null && user.getUsername().equals(AppContext.getUsername())) {
            isRefreshable = true;
        }

        UserService userService = AppContextUtil.getSpringBean(UserService.class);
        user.setAccountId(AppContext.getAccountId());
        user.setSubdomain(AppContext.getSubDomain());

        if (user.getStatus() == null) {
            user.setStatus(UserStatusConstants.EMAIL_VERIFIED_REQUEST);
        }

        if (user.getUsername() == null) {
            if (user.getPassword() == null) {
                user.setPassword(RandomPasswordGenerator.generateRandomPassword());
            }
            String userPassword = user.getPassword();
            userService.saveUserAccount(user, user.getRoleid(), AppContext.getSubDomain(), AppContext.getAccountId(), AppContext.getUsername(), true);
            UI.getCurrent().addWindow(new NewUserAddedWindow(user, userPassword));
        } else {
            userService.updateUserAccount(user, AppContext.getAccountId());
            EventBusFactory.getInstance().post(new UserEvent.GotoList(this, null));
        }

        if (isRefreshable) {
            Page.getCurrent().getJavaScript().execute("window.location.reload();");
        }
    }

    @Override
    protected void onGo(ComponentContainer container, ScreenData<?> data) {
        UserContainer userContainer = (UserContainer) container;
        userContainer.removeAllComponents();
        userContainer.addComponent(view);

        SimpleUser user = (SimpleUser) data.getParams();
        if (user.getUsername() != null) {
            view.editItem(user, false);
        } else {
            view.editItem(user);
        }

        AccountSettingBreadcrumb breadcrumb = ViewManager.getCacheComponent(AccountSettingBreadcrumb.class);

        if (user.getUsername() == null) {
            breadcrumb.gotoUserAdd();
        } else {
            breadcrumb.gotoUserEdit(user);
        }
    }
}