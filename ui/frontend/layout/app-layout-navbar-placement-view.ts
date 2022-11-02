import {customElement, html, LitElement} from "lit-element";

@customElement('app-layout-navbar-placement-view')
export class AppLayoutNavbarPlacementView extends LitElement {
    // static get styles() {
    //   return css`
    //     :host {
    //         display: block;
    //         height: 100%;
    //     }
    //     `;
    // }

    render() {
        return html`
            <vaadin-app-layout>
                <vaadin-drawer-toggle slot="navbar"></vaadin-drawer-toggle>
                <h1 slot="navbar">Rika2Mqtt</h1>
                <vaadin-tabs slot="drawer" orientation="vertical">
                    <vaadin-tab>
                        <a tabindex="-1">
                            <vaadin-icon icon="vaadin:fire"></vaadin-icon> 
                            <span>Dashboard</span>
                        </a>
                    </vaadin-tab>
                    <vaadin-tab>
                        <a tabindex="-1">
                            <vaadin-icon icon="vaadin:clock"></vaadin-icon> 
                            <span>Activity</span>
                        </a>
                    </vaadin-tab>
                    <vaadin-tab>
                        <a tabindex="-1">
                            <vaadin-icon icon="vaadin:cogs"></vaadin-icon> 
                            <span>Settings</span>
                        </a>
                    </vaadin-tab>
                    <vaadin-tab>
                        <a tabindex="-1">
                            <vaadin-icon icon="vaadin:desktop"></vaadin-icon> 
                            <span>System</span>
                        </a>
                    </vaadin-tab>
                </vaadin-tabs>
            </vaadin-app-layout>
        `;
    }

    // Remove this method to render the contents of this view inside Shadow DOM
    createRenderRoot() {
        return this;
    }
}
