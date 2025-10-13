<nav th:fragment="subnav(active)"
     class="mm-subnav"
     aria-label="Secciones de exploración">
    <a th:href="@{/home}"
       th:classappend="${active} == 'home' ? ' is-active' : ''">
        Explorar
    </a>

    <a th:href="@{/collections}"
       th:classappend="${active} == 'collections' ? ' is-active' : ''">
        Colecciones
    </a>

    <a th:href="@{/map}"
       th:classappend="${active} == 'map' ? ' is-active' : ''">
        Mapa
    </a>
</nav>
