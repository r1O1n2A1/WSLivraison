(function() {
    'use strict';
    angular
        .module('wsLivraisonApp')
        .factory('Postman', Postman);

    Postman.$inject = ['$resource'];

    function Postman ($resource) {
        var resourceUrl =  'api/postmen/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
